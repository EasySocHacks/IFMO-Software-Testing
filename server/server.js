const express = require('express')
const bodyParser = require('body-parser')
const session = require('express-session');
const sqlite = require('sqlite3').verbose()
const cookieParser = require('cookie-parser')
global.crypto = require('crypto')

const url = "localhost";
const port = 4000;
const version = "0.1";
const db = new sqlite.Database('./server_db')

//TODO .env
const SALT = "dnasjodhausudf2ur28fhwcuh23d23dd23dre234fref4r4tghthljdi32";

run();

function run() {
    const api = express();
    api.use(bodyParser.json());
    api.use(express.urlencoded());
    api.use(cookieParser());
    api.use(session({
        secret: 'bufbIUDf9g(&G^T0-32hhdubusgugsgbsduHUH',
        name: 'userSession',
        keys: ['key1', 'key2'],
        cookie: {
            expires: new Date( Date.now() + 60 * 60 * 1000 )
        }
    }));
    api.options('http://' + url + ':' + port.toString());
    api.use(function (req, res, next) {
        res.setHeader('Access-Control-Allow-Origin', 'http://localhost:3000');
        res.setHeader('Access-Control-Allow-Credentials', 'true');
        res.setHeader('Cache-Control', 'no-store');

        next();
    });

    //TODO add friend request
    api.get(getVersionUrl('/friends'), (req, res) => {
        if (req.session.userId) {
            dbQuery(`select * from Users where id != ${req.session.userId};`,
                (rows) => {
                    res.send({
                        data: rows.map((row) => {
                            return {
                                id: row.id,
                                name: row.name,
                                age: row.age
                            };
                        })
                    });
                }, () => {})
        } else {
            res.send({data: []});
        }
    });

    api.get(getVersionUrl('/friend'), (req, res) => {
        if (req.session.userId) {
            if (!req.query.id) {
                res.status(404);
            } else {
                dbQuery(`select * from Users where id = ${req.query.id};`,
                    (rows) => {
                        if (!rows || rows.length === 0) {
                            res.send({data: {}});
                        } else {
                            res.send({
                                data: {
                                    id: rows[0].id,
                                    name: rows[0].name,
                                    age: rows[0].age
                                }
                            });
                        }
                    })
            }
        } else {
            res.send({data: {}});
        }
    });

    api.get(getVersionUrl("/chatWith"), (req, res) => {
        if (req.session.userId) {
            if (!req.query.id) {
                res.status(404);
            } else {
                dbQuery(`select * from Messages where 
                             (from_user_id = ${req.session.userId} and to_user_id = ${req.query.id}) or 
                             (from_user_id = ${req.query.id} and to_user_id = ${req.session.userId})`,
                    (rows) => {
                        res.send({
                            data: rows.map((row) => {
                                return {
                                    from: row.from_user_id,
                                    to: row.to_user_id,
                                    message: row.message
                                };
                            })
                        });
                    });
            }
        } else {
            res.send({data: []});
        }
    });

    //TODO verify
    api.post(getVersionUrl("/register"), (req, res) => {
        const login = req.body.login;
        const password = saltPasswordSha(req.body.password);
        const age = req.body.age;

        dbQuery(`insert into Users(name, password, age) values ('${login}', '${password}', ${age});`,
            (rows) => {
                dbQuery(`select * from Users where name = '${login}';`,
                    (rows) => {
                        req.session.userId = rows[0].id;

                        res.redirect(301, "http://localhost:3000");
                        res.end();
                    },
                    () => {})
            },
            (err) => {
                res.redirect(301, "http://localhost:3000/register");
                res.end();
            });
    });

    //TODO verify
    api.post(getVersionUrl("/login"), (req, res) => {
        const login = req.body.login;
        const password = saltPasswordSha(req.body.password);

        dbQuery(`select * from Users where name = '${login}' and password = '${password}';`,
            (rows) => {
                req.session.userId = rows[0].id;

                res.redirect(301, "http://localhost:3000");
                res.end();
            },
            (err) => {
                res.redirect(301, "http://localhost:3000/login");
                res.end();
            })
    });

    api.get(getVersionUrl("/logout"), (req, res) => {
        req.session.destroy();

        res.redirect(301, "http://localhost:3000");
        res.end();
    });

    api.get(getVersionUrl("/me"), (req, res) => {
        if (req.session.userId) {
            dbQuery(`select * from Users where id=${req.session.userId};`,
                (rows) => {
                    res.send({data: {id: rows[0].id, name: rows[0].name, age: rows[0].age}});
                },
                () => {})
        } else {
            res.send({data: {}});
        }
    });

    api.post(getVersionUrl("/sendMessage"), (req, res) => {
        if (req.session.userId) {
            const message = req.body.message;
            const receiverId = parseInt(req.body.receiver);

            dbQuery(`insert into Messages(from_user_id, to_user_id, message) values (${req.session.userId}, ${receiverId}, '${message}');`,
                (rows) => {
                    res.redirect(301, "http://localhost:3000/friend?id=" + receiverId);
                    res.end();
                }, (err) => {console.log(err)})
        } else {
            res.redirect(301, "http://localhost:3000/login");
            res.end();
        }
    });

    api.listen(port);
}

function getVersionUrl(url) {
    return '/api/v' + version + url;
}

function dbQuery(query, responseHandler, errorHandler) {
    db.all(query, [], (err, rows) => {
        if (err) {
            errorHandler(err);
        } else {
            responseHandler(rows);
        }
    });
}

function saltPasswordSha(password) {
    return crypto.createHash('sha256').update(password + SALT).digest('base64');
}
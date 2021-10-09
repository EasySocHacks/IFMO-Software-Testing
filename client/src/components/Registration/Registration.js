export default function Registration() {
    return (
        <div>
            <form method="post"  name="register" autoComplete="off" action={process.env.REACT_APP_SERVER_URL + "/register"}>
                <div>
                    <p>Login: </p>
                    <input type="text" name="login"/>
                </div>
                <div>
                    <p>Age: </p>
                    <input type="text" name="age"/>
                </div>
                <div>
                    <p>Password: </p>
                    <input type="password" name="password"/>
                </div>
                <div>
                    <input type="submit" value="Register"/>
                </div>
            </form>
        </div>
    );
}
import "./Header.css"
import {useEffect, useState} from "react";

async function logout() {
    await fetch("http://localhost:8080/users/logout", {
        credentials: "include"
    })

    window.location = "/"
}

export default function Header() {
    const [user, setUser] = useState({});

    useEffect(async () => {
        setUser(await (await fetch("http://localhost:8080/me", {
            credentials: "include"
        })).json());
    }, []);

    if (user.id) {
        return (
            <div className="header">
                <div>
                    <div className="userName">
                        <span>{user.name}</span>
                    </div>
                    <div className="logoutTab" onClick={logout}>
                        Logout
                    </div>
                </div>
                <a className="headerTabLink" href="/friends">
                    <div className="headerTab">
                        Friends
                    </div>
                </a>
                <a className="headerTabLink" href="/users">
                    <div className="headerTab">
                        Users
                    </div>
                </a>
            </div>
        );
    } else {
        return (
            <div className="header">
                <div>
                    <a className="registrationTabLink" href="/register">
                        <div className="registrationTab">
                            Register
                        </div>
                    </a>
                    <a className="loginTabLink" href="/login">
                        <div className="loginTab">
                            Login
                        </div>
                    </a>
                </div>
                <a className="headerTabLink" href="/friends">
                    <div className="headerTab">
                        Friends
                    </div>
                </a>
                <a className="headerTabLink" href="/users">
                    <div className="headerTab">
                        Users
                    </div>
                </a>
            </div>
        );
    }
}
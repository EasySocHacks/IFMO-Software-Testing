import "./Header.css"
import {useEffect, useState} from "react";

export default function Header() {
    const [user, setUser] = useState({});

    useEffect(async () => {
        setUser((await (await fetch(process.env.REACT_APP_SERVER_URL + "/me", {
            credentials: "include"
        })).json()).data);
    }, []);

    if (user.id) {
        return (
            <div className="header">
                <div>
                    <div className="userName">
                        <span>{user.name}</span>
                    </div>
                    <a className="logoutTabLink" href="/logout">
                        <div className="logoutTab">
                            Logout
                        </div>
                    </a>
                </div>
                <a className="friendsTabLink" href="/friends">
                    <div className="friendsTab">
                        Friends
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
                <a className="friendsTabLink" href="/friends">
                    <div className="friendsTab">
                        Friends
                    </div>
                </a>
            </div>
        );
    }
}
import {useEffect, useState} from "react";
import {Redirect} from "react-router-dom";
import "./FriendPage.css"
import Chat from "../../components/Chat/Chat";

export default function FriendPage() {
    const [friend, setFriend] = useState({});
    const [messages, setMessages] = useState([]);
    const queryParams = new URLSearchParams(window.location.search);

    useEffect(async () => {
        if (queryParams.get("id")) {
            setFriend(await (await fetch("http://localhost:8080/friend?id=" + queryParams.get("id"), {
                credentials: "include"
            })).json());
            setMessages(await (await fetch("http://localhost:8080/friend?id=/chat?id=" + queryParams.get("id"), {
                credentials: "include"
            })).json())
        }
    }, []);

    if (queryParams.get("id") && friend !== undefined) {
        return (
            <>
                <div className="friend">
                    <h1>{friend.name}#{friend.id}</h1>
                    <h5>age: {friend.age}</h5>
                </div>
                <Chat messages={messages}/>
            </>
        );
    } else {
        return <Redirect to="/404"/>;
    }
}
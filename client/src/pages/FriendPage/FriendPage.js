import {useEffect, useState} from "react";
import {Redirect} from "react-router-dom";
import "./FriendPage.css"
import Chat from "../../components/Chat/Chat";

export default function FriendPage() {
    const [friend, setFriend] = useState({});
    const [messages, setMessages] = useState([]);
    const queryParams = new URLSearchParams(window.location.search);

    //TODO id
    //TODO token requirement
    useEffect(async () => {
        if (queryParams.get("id")) {
            setFriend((await (await fetch(process.env.REACT_APP_SERVER_URL + "/friend?id=" + queryParams.get("id"), {
                credentials: "include"
            })).json()).data);
            setMessages((await (await fetch(process.env.REACT_APP_SERVER_URL + "/chatWith?id=" + queryParams.get("id"), {
                credentials: "include"
            })).json()).data)
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
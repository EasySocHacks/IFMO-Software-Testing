import React, {useEffect, useState} from "react";
import FriendList from "../../components/FriendList/FriendList";
import "./FriendListPage.css"

export default function FriendListPage() {
    const [friendList, setFriendList] = useState([]);

    useEffect(async () => {
        setFriendList((await (await fetch(process.env.REACT_APP_SERVER_URL + "/friends", {
            credentials: "include"
        })).json()).data);
    }, []);

    return (
        <div className="friendList">
            <FriendList friendList={friendList}/>
        </div>
    );
}
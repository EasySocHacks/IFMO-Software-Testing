import React, {useEffect, useState} from "react";
import FriendList from "../../components/FriendList/FriendList";
import "./FriendListPage.css"

export default function FriendListPage() {
    const [friendList, setFriendList] = useState([]);

    useEffect(async () => {
        setFriendList(await (await fetch( "http://localhost:8080/friends", {
            credentials: "include"
        })).json());
    }, []);

    return (
        <div className="friendList">
            <FriendList friendList={friendList}/>
        </div>
    );
}
import {useEffect, useState} from "react";
import UserList from "../../components/UserList/UserList";

export default function UserListPage() {
    const [userList, setUserList] = useState([]);

    useEffect(async () => {
        setUserList(await (await fetch( "http://localhost:8080/friends/nonFriendUsers", {
            credentials: "include"
        })).json());
    }, []);

    return (
        <>
            <UserList userList={userList}/>
        </>
    );
}
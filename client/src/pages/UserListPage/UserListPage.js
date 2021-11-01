import {useEffect, useState} from "react";
import UserList from "../../components/UserList/UserList";

export default function UserListPage() {
    const [userList, setUserList] = useState([]);

    useEffect(async () => {
        setUserList(await (await fetch( process.env.REACT_APP_SERVER_URL + "/friends/nonFriendUsers", {
            credentials: "include"
        })).json());
    }, []);

    return (
        <>
            <UserList userList={userList}/>
        </>
    );
}
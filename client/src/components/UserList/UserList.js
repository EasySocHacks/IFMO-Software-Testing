import type {UserListElementInterface} from "../UserListElement/UserListElement";
import UserListElement from "../UserListElement/UserListElement";
import './UserList.css'

export interface UserListInterface {
    userList: UserListElementInterface[],
}

export default function UserList(props: UserListInterface) {
    return (
        <div className="friendListDiv">
            <h3>Users</h3>
            <ul className="userList">
                {props.userList.map(userListElement => {
                    return (
                        <li className="userListElement">
                            <UserListElement
                                id={userListElement.id}
                                name={userListElement.name}
                                age={userListElement.age}/>
                        </li>
                    );
                })}
            </ul>
        </div>
    );
}
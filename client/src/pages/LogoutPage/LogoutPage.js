import {useEffect} from "react";

export default function LogoutPage() {
    useEffect( () => {
        (fetch(process.env.REACT_APP_SERVER_URL + "/logout", {
            credentials: "include"
        }))
    }, []);

    return (
        <></>
    );
}
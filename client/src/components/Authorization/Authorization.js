export default function Authorization() {
    return (
        <div>
            <form method="post"  name="login" autoComplete="off" action={process.env.REACT_APP_SERVER_URL + "/login"}>
                <div>
                    <p>Login: </p>
                    <input type="text" name="login"/>
                </div>
                <div>
                    <p>Password: </p>
                    <input type="password" name="password"/>
                </div>
                <div>
                    <input type="submit" value="Login"/>
                </div>
            </form>
        </div>
    );
}
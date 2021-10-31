import './Authorization.css'

export default function Authorization() {
    return (
        <div>
            <iframe name="dummyFrame" id="dummyFrame"/>

            <form method="post" name="login" autoComplete="off" action="http://localhost:8080/users/login" target="dummyFrame">
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
import './Authorization.css'

async function redirect() {
    window.location = "/"
}

export default function Authorization() {
    return (
        <div>
            <iframe title="AuthorizationIFrame" name="dummyFrame" id="dummyFrame"/>

            <form method="post" name="login" autoComplete="off" action={process.env.REACT_APP_SERVER_URL + "/users/login"}
                  target="dummyFrame" onSubmit={redirect}>
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
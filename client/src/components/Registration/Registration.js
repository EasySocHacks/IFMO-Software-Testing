export default function Registration() {
    return (
        <div>
            <form method="post"  name="register" autoComplete="off" action={process.env.REACT_APP_SERVER_URL + "/register"}>
                <div className="login-field">
                    <p>Login: </p>
                    <input type="text" name="login"/>
                </div>
                <div className="age-field">
                    <p>Age: </p>
                    <input type="text" name="age"/>
                </div>
                <div className="password-field">
                    <p>Password: </p>
                    <input type="password" name="password"/>
                </div>
                <div className="submit-field">
                    <input type="submit" value="Register"/>
                </div>
            </form>
        </div>
    );
}
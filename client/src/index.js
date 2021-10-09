import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import {createBrowserHistory} from "history";
import {Route, Router, Switch} from "react-router-dom";
import {QueryClient, QueryClientProvider} from "react-query";
import FriendPage from "./pages/FriendPage/FriendPage";
import FriendListPage from "./pages/FriendListPage/FriendListPage";
import NotFound from "./components/NotFound/NotFound";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import {config} from "dotenv";
import RegistrationPage from "./pages/RegistrationPage/RegistrationPage";
import AuthorizationPage from "./pages/AuthorizationPage/AuthorizationPage";
import LogoutPage from "./pages/LogoutPage/LogoutPage";

config()

const history = createBrowserHistory()
const queryClient = new QueryClient();

ReactDOM.render(
    <React.StrictMode>
        <QueryClientProvider client={queryClient}>
            <Header/>
            <Router history={history}>
                <Switch>
                    <Route path="/friends">
                        <FriendListPage/>
                    </Route>
                    <Route path="/friend">
                        <FriendPage/>
                    </Route>
                    <Route path="/register">
                        <RegistrationPage/>
                    </Route>
                    <Route path="/login">
                        <AuthorizationPage/>
                    </Route>
                    <Route path="/logout">
                        <LogoutPage/>
                    </Route>
                    <Route path="/404">
                        <NotFound/>
                    </Route>
                    <Route exact path="/">
                        <FriendListPage/>
                    </Route>
                    <Route>
                        <NotFound/>
                    </Route>
                </Switch>
            </Router>
            <Footer/>
        </QueryClientProvider>
    </React.StrictMode>,
    document.getElementById('root')
);

reportWebVitals();

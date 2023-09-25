import React from "react";
import "./App.css";
import Header from "./components/Header";
import Nav from "./components/Nav";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import ValidateEmail from "./pages/ValidateEmail";
import NewPassword from "./pages/NewPassword";
import BoardList from "./pages/BoardList";
import Home from "./pages/Home";

function App() {
	return (
		<div className="App">
			<Router>
				<Header isLogin={false} />
				<div className="page_wrap">
					<Nav />
					<main className="container">
						<Routes>
							<Route path="/" element={<Home/>}/>
							<Route path="/login" element={<Login />} />
							<Route path="/signup" element={<Signup />} />
							<Route path="/validateEmail" element={<ValidateEmail />} />
							<Route path="/newPassword" element={<NewPassword />} />
							<Route path="/board" element={<BoardList />} />
						</Routes>
					</main>
				</div>
			</Router>
		</div>
	);
}

export default App;

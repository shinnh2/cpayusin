import React from "react";
import "./App.css";
import Header from "./components/Header";
import Nav from "./components/Nav";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import ValidateEmail from "./pages/ValidateEmail";

function App() {
	return (
		<div className="App">
			<Router>
				<Header isLogin={false} />
				<div className="page_wrap">
					<Nav />
					<main className="container">
						<Routes>
							<Route path="/login" element={<Login />} />
							<Route path="/signup" element={<Signup />} />
							<Route path="/validateEmail" element={<ValidateEmail />} />
						</Routes>
					</main>
				</div>
			</Router>
		</div>
	);
}

export default App;

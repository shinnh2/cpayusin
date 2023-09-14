import React from "react";
import "./App.css";
import Header from "./components/Header";
import Nav from "./components/Nav";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import Signup from "./pages/Signup";

function App() {
	return (
		<div className="App">
			<Header isLogin={false} />
			<div className="page_wrap">
				<Nav />
				<main className="container">
					<Router>
						<Routes>
							<Route path="/login" element={<Login />} />
							<Route path="/signup" element={<Signup />} />
						</Routes>
					</Router>
				</main>
			</div>
		</div>
	);
}

export default App;

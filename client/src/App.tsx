import React from "react";
import "./App.css";
import Header from "./components/Header";
import Nav from "./components/Nav";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";

function App() {
	return (
		<div className="App">
			<Header isLogin={false} />
			<div className="page_wrap">
				<Nav />
				<main className="container">
					<Router>
						<Routes>
							<Route path="/login" Component={Login} />
						</Routes>
					</Router>
				</main>
			</div>
		</div>
	);
}

export default App;

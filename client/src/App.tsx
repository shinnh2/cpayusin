import React from "react";
import "./App.css";
import Button from "./components/Button";

function App() {
	return (
		<div className="App">
			<header>JBaccount HOME</header>
			<section>
				<Button buttonType="no_em" buttonSize="big" buttonLabel="테스트" />
			</section>
		</div>
	);
}

export default App;

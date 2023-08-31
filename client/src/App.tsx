import React from "react";
import "./App.css";
import Button from "./components/Button";
import Input from "./components/Input";

function App() {
	return (
		<div className="App">
			<header>JBaccount HOME</header>
			<section>
				<Button buttonType="no_em" buttonSize="big" buttonLabel="테스트" />
				<Input
					InputLabel="테스트"
					isLabel={true}
					errorMsg="이것은 오류일 때 나타납니다"
					inputAttr={{ type: "text", placeholder: "입력하세요" }}
				/>
			</section>
		</div>
	);
}

export default App;

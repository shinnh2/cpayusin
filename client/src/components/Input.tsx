import React, { ChangeEvent } from "react";
import "../App.css";

export interface InputProps {
	InputLabel: string; //label명
	isLabel: boolean; //label의 유무를 나타냅니다.
	//input 속성
	inputAttr: {
		type: string;
		placeholder: string;
		id?: string;
	};
	errorMsg?: string;
	children?: React.ReactNode; //버튼이 포함된 경우
	setInputValue?: (value: string) => void; //값 설정 함수
	inputValue?: string; //값
	isError?: boolean; //에러 여부
}

const Input = ({
	InputLabel,
	isLabel,
	errorMsg,
	inputAttr,
	children,
	setInputValue,
	inputValue,
	isError,
}: InputProps) => {
	const id = `jb-input-${Math.random()}` ?? inputAttr.id;
	const handleOnchange = (event: ChangeEvent<HTMLInputElement>) => {
		if (setInputValue !== undefined)
			setInputValue((event.target as HTMLInputElement)?.value);
	};
	return (
		<div className="input_wrap">
			{isLabel ? <label htmlFor={id}>{InputLabel}</label> : null}
			<div className="input_area">
				<input
					id={id}
					type={inputAttr.type}
					placeholder={inputAttr.placeholder}
					onChange={handleOnchange}
					value={inputValue}
					className={isError ? "error" : ""}
				/>
				{children && children}
			</div>

			{isError ? <p className="error_msg">{errorMsg}</p> : null}
		</div>
	);
};
export default Input;

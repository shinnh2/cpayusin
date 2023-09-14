import React from "react";
import "../App.css";

export interface InputProps {
	InputLabel: string;
	isLabel: boolean; //label의 유무를 나타냅니다.
	inputAttr: {
		type: string;
		placeholder: string;
		id?: string;
	};
	errorMsg?: string;
	children?: React.ReactNode;
}

let count = 0;

const Input = ({
	InputLabel,
	isLabel,
	errorMsg,
	inputAttr,
	children,
}: InputProps) => {
	const id = `jb-input-${count + 1}` ?? inputAttr.id;
	return (
		<div className="input_wrap">
			{isLabel ? <label htmlFor={id}>{InputLabel}</label> : null}
			<div className="input_area">
				<input
					id={id}
					type={inputAttr.type}
					placeholder={inputAttr.placeholder}
				/>
				{children && children}
			</div>

			{errorMsg ? <p className="error_msg">{errorMsg}</p> : null}
		</div>
	);
};
export default Input;

///////////////

// import colors from "_tosslib/constants/colors";
// import useId from "_tosslib/hooks/useId";
// import {
// 	Children,
// 	cloneElement,
// 	ForwardedRef,
// 	forwardRef,
// 	HTMLAttributes,
// 	InputHTMLAttributes,
// 	ReactElement,
// 	ReactNode,
// } from "react";

// interface InputProps extends HTMLAttributes<HTMLDivElement> {
// 	label?: ReactNode;
// 	children: ReactElement;
// 	bottomText?: string;
// }

// export function Input({ label, children, bottomText, ...props }: InputProps) {
// 	const child = Children.only(children); //하위 요소를 하나만 받는다
// 	const generatedId = useId("input"); //커스텀 훅을 사용해 id를 생성
// 	const id = child.props.id ?? generatedId; //child의 id가 없을 경우 기본값으로 훅으로 생성된 id로 설정한다.
// 	const isError: boolean = child.props.error ?? false; //왜 있지.. 에러가 있다면 표시, 기본값은 false

// 	return (
// 		<div {...props}>
// 			<label htmlFor={id}>{label}</label>
// 			{cloneElement(child, {
// 				id,
// 				...child.props,
// 			})}
// 			{bottomText != null ? <p>{bottomText}</p> : null}
// 		</div>
// 	);
// }

// interface TextFieldProps
// 	extends Omit<InputHTMLAttributes<HTMLInputElement>, "size"> {
// 	error?: boolean;
// }

// Input.TextField = forwardRef(
// 	(
// 		{ error, ...props }: TextFieldProps,
// 		ref: ForwardedRef<HTMLInputElement>
// 	) => {
// 		return <input ref={ref} {...props} />;
// 	}
// );

import Input from "./../components/Input";
import Button from "./../components/Button";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import { validator, ValidatorStatus } from "../assets/validater";
import { userDataType } from "./../App";
import { useState } from "react";
import { getAccessToken } from "../assets/tokenActions";

const NewPassword = ({ userData }: { userData: userDataType }) => {
	const api = process.env.REACT_APP_API_URL;
	const navigate = useNavigate();
	const [form, setForm] = useState({
		email: userData.email,
		password: "",
	});
	const [isError, setIsError] = useState(false);
	const [newPWChk, setNewPWChk] = useState("");

	const setNewPasswordValue = (value: string) => {
		setForm((prevState) => ({
			...prevState,
			password: value,
		}));
	};
	const setNewPasswordChkValue = (value: string) => {
		setNewPWChk(value);
	};

	const validatorStatusPassword: ValidatorStatus = {
		value: form.password,
		isRequired: true,
		valueType: "password",
	};
	const handleSubmitNewPW = () => {
		setIsError(!validator(validatorStatusPassword));
		if (isError) {
			return;
		}
		if (form.password !== newPWChk) {
			alert("비밀번호 확인을 잘못 입력했습니다.");
			return;
		}

		const config = {
			headers: {
				Authorization: `${getAccessToken()}`,
			},
		};

		axios
			.patch(`${api}/api/v1/reset-password`, form, config)
			.then((_) => {
				alert("비밀번호가 변경되었습니다.");
				navigate(`/user/${userData?.id}`);
			})
			.catch((error) => {
				alert("비밀번호 변경에 실패하였습니다.");
				console.error(error);
			});
	};

	return (
		<div className="input_box newpassword_box col_4">
			<h3 className="title_h3">새 비밀번호 입력</h3>
			<div className="content">
				<Input
					InputLabel="새 비밀번호 입력"
					isLabel={true}
					errorMsg="비밀번호는 8~20자의 영문, 숫자가 포함되어야 합니다."
					inputAttr={{
						type: "password",
						placeholder: "새 비밀번호를 입력하세요",
					}}
					setInputValue={setNewPasswordValue}
					inputValue={form.password}
				/>
				<Input
					InputLabel="새 비밀번호 확인"
					isLabel={true}
					errorMsg="비밀번호가 일치하지 않습니다."
					inputAttr={{
						type: "password",
						placeholder: "새 비밀번호를 한 번 더 입력하세요",
					}}
					setInputValue={setNewPasswordChkValue}
					inputValue={newPWChk}
				/>
				<Button
					buttonType="primary"
					buttonSize="big"
					buttonLabel="입력 완료"
					onClick={handleSubmitNewPW}
				/>
			</div>
		</div>
	);
};
export default NewPassword;

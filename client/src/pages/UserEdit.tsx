import Input from "../components/Input";
import Button from "../components/Button";
import { userDataType } from "./../App";
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { validator, ValidatorStatus } from "../assets/validater";
import {
	getAccessToken,
	isAccessToken,
	removeAccessToken,
} from "../assets/tokenActions";

const UserEdit = ({
	userData,
	setIsLogin,
}: {
	userData: userDataType | undefined;
	setIsLogin: React.Dispatch<React.SetStateAction<boolean>>;
}) => {
	const api = process.env.REACT_APP_API_URL;
	const params = useParams();
	const navigate = useNavigate();
	const [fileName, setFileName] = useState("이미지 파일을 선택하세요");
	const [file, setFile] = useState("");
	const [userNickname, setUserNickname] = useState("");
	const [isNameError, setIsNameError] = useState(false);

	const handleSetNickname = (value: string) => {
		setUserNickname(value);
	};
	const validatorStatusNickname: ValidatorStatus = {
		value: userNickname!,
		isRequired: true,
		valueType: "nickname",
	};

	useEffect(() => {
		let isToken = isAccessToken();
		if (isToken === false) {
			alert("로그인이 필요한 서비스입니다.");
			navigate(`/login`);
		}
	}, []);

	const handleOnChange = (event: any) => {
		setFileName(event.currentTarget.files[0].name);
		setFile(event.currentTarget.files[0]);
	};

	const handleSubmit = () => {
		setIsNameError(!validator(validatorStatusNickname));
		if (isNameError) return;
		const data = {
			nickname: userNickname,
			//비밀번호 받아야 함. 그런데 그러면 화면을 수정해야할 필요가 있으므로 논의할 것
		};

		const formData = new FormData();
		formData.append(
			"data",
			new Blob([JSON.stringify(data)], {
				type: "application/json",
			})
		);
		if (file) {
			formData.append("image", new Blob([JSON.stringify(file)]));
		}
		const postAxiosConfig = {
			headers: {
				"Content-Type": "multipart/form-data", // FormData를 사용할 때 Content-Type을 변경
				Authorization: `${getAccessToken()}`,
			},
		};
		axios
			.patch(`${api}/api/v1/member/update`, formData, postAxiosConfig)
			.then((_) => {
				alert("회원 정보가 수정되었습니다.");
				navigate(`/user/${userData?.id}`);
			})
			.catch((error) => {
				alert("회원 정보 수정에 실패하였습니다.");
				setUserNickname(userData?.nickname!);
				if (error.response) {
					// 서버 응답이 있을 경우 (에러 상태 코드가 반환된 경우)
					console.error("서버 응답 에러:", error.response.data);
					console.error("응답 상태 코드:", error.response.status);
					console.error("응답 헤더:", error.response.headers);
				} else if (error.request) {
					// 요청이 전혀 되지 않았을 경우
					console.error("요청 에러:", error.request);
				} else {
					// 설정에서 문제가 있어 요청이 전송되지 않은 경우
					console.error("Axios 설정 에러:", error.message);
				}
				console.error("에러 구성:", error.config);
			});
	};

	const handleClickMemberDelete = () => {
		const config = {
			headers: { Authorization: `${getAccessToken()}` },
		};
		//팝업창으로 대체할 것
		let isDeleteYes = window.confirm("정말로 탈퇴하시겠습니까?");
		if (isDeleteYes) {
			axios
				.delete(`${api}/api/v1/member/delete`, config)
				.then((_) => {
					alert("탈퇴되었습니다.");
					removeAccessToken();
					setIsLogin(false);
					navigate(`/`);
				})
				.catch((error) => {
					alert("회원 탈퇴에 실패하였습니다.");
				});
		}
	};

	return (
		<div className="user_edit_wrap">
			<div className="user_profile">
				<div className="profile_img_wrap"></div>
				<div className="user_info_wrap">
					<p className="title_h5">{userData?.nickname}</p>
					<p className="email">{userData?.email}</p>
				</div>
			</div>
			<div className="user_input_content_wrap">
				<Input
					InputLabel="닉네임 수정"
					isLabel={true}
					errorMsg="중복된 닉네임입니다. 혹은 공백 포함 2~10자 사이의 영문, 한글, 숫자만 가능합니다."
					inputAttr={{
						type: "text",
						placeholder: "수정할 닉네임을 입력하세요",
					}}
					setInputValue={handleSetNickname}
					inputValue={userNickname}
					isError={isNameError}
				>
					<Button
						buttonType="another"
						buttonSize="big"
						buttonLabel="중복 확인"
					/>
				</Input>
				<Input
					InputLabel="프로필 이미지 수정"
					isLabel={true}
					errorMsg="이미지를 다시 업로드 해주세요."
					inputAttr={{
						type: "text",
						placeholder: "이미지를 업로드하세요",
					}}
					inputValue={fileName}
					isReadonly={true}
				>
					<button className="file_upload big">
						파일 선택
						<input
							type="file"
							onChange={(event) => handleOnChange(event)}
							id="selectImg"
							className="file_upload"
						/>
					</button>
				</Input>
				<button className="link" onClick={handleClickMemberDelete}>
					회원 탈퇴하기
				</button>
			</div>
			<div className="user_edit_button_wrap">
				<Button
					buttonType="primary"
					buttonSize="big"
					buttonLabel="수정 완료"
					onClick={handleSubmit}
				/>
				<Button
					buttonType="no_em"
					buttonSize="big"
					buttonLabel="취소"
					onClick={() => navigate(`/user/${userData?.id}/edit`)}
				/>
			</div>
		</div>
	);
};
export default UserEdit;

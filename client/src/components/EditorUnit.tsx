import React, { useEffect, useState } from "react";
import { useRef } from "react";
import { Editor } from "@toast-ui/react-editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import "@toast-ui/editor/dist/i18n/ko-kr";
import Button from "./Button";
import axios from "axios";

type HookCallback = (url: string, text?: string) => void;

const EditorUnit = React.forwardRef((props: any, ref) => {
	const api = process.env.REACT_APP_API_URL;
	const editorRef = useRef<Editor>(null);
	const latestBoardDataRef = useRef(props.nowBoardData); // 최신 상태를 저장할 ref

	//Editor의 내용을 가져오는 함수
	const getInstance = () => {
		return editorRef.current?.getInstance();
	};

	React.useImperativeHandle(ref, () => ({
		getInstance,
	}));

	const handleImageUpload = (blob: Blob | any, callback: HookCallback) => {
		/*
		실행되는 시점: 이미지를 업로드하고 '확인'버튼을 클릭했을 때, 이미지 붙여넣기 했을 때
		-> callback이 실행되지 않으면 이미지 업로드 창이 닫히지 않는다.
		callback(`${blob.name}`, `${blob.name}`); //이미지 저장된 url, img 대체 텍스트
		callback사용용도: 이미지를 먼저 서버에 업로드하여 응답으로 오는 url이나 파일명을 html에 처리한다.
		*/

		//이미지 API 적용
		//이미지 대체 텍스트 처리
		const blobName = blob.name === "" ? "사용자 이미지" : blob.name;
		const newFormData = new FormData();
		newFormData.append("file", blob);
		const postAxiosConfig = {
			headers: {
				"Content-Type": "multipart/form-data", // FormData를 사용할 때 Content-Type을 변경
			},
		};
		axios
			.post(`${api}/api/v1/file`, newFormData, postAxiosConfig)
			.then((response) => {
				console.log(response.data.data.url);
				callback(`${response.data.data.url}`, `${blobName}`); //이미지 저장된 url, img 대체 텍스트
			})
			.catch((error) => {
				console.log(error);
				alert("이미지가 정상적으로 업로드 되지 않았습니다.");
			});
	};

	return (
		<div className="editor_unit">
			<Editor
				initialValue=" "
				previewStyle="vertical"
				height="600px"
				language="ko-KR"
				initialEditType="wysiwyg"
				useCommandShortcut={true}
				ref={editorRef}
				hooks={{
					addImageBlobHook: handleImageUpload,
				}}
			/>
		</div>
	);
});

export default EditorUnit;

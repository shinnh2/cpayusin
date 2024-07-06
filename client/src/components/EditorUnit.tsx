import React, { useEffect, useState } from "react";
import { useRef } from "react";
import { Editor } from "@toast-ui/react-editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import "@toast-ui/editor/dist/i18n/ko-kr";
import Button from "./Button";
import axios from "axios";

type HookCallback = (url: string, text?: string) => void;

const EditorUnit = React.forwardRef((props: any, ref) => {
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
		//실행되는 시점: 이미지를 업로드하고 '확인'버튼을 클릭했을 때.
		//callback이 실행되지 않으면 이미지 업로드 창이 닫히지 않는다.
		//callback(`${blob.name}`, `${blob.name}`); //이미지 저장된 url, img 대체 텍스트
		//callback사용용도: 이미지를 먼저 서버에 업로드하여 응답으로 오는 url이나 파일명을 html에 처리한다.

		//blob데이터 저장
		props.setBlobData(blob);

		//일단 이미지 그대로 두기
		const reader = new FileReader();
		reader.readAsDataURL(blob);
		reader.onloadend = () => {
			const base64data = reader.result;
			callback(`${base64data}`, `${blob.name}`); //이미지 저장된 url, img 대체 텍스트
		};
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

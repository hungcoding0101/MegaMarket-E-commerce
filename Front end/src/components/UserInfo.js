import { Button, Col, Form, Input, Modal, Result, Row, Skeleton, Spin } from 'antd'
import {
  LockOutlined,
  EyeTwoTone,
  EyeInvisibleOutlined,
} from "@ant-design/icons";
import React, { useContext, useState } from 'react'
import { useCurrentUser, useUpdateBasicInfos } from '../Hooks/UserHooks';
import { appContext } from '../App';

export default function UserInfo() {
  const [form] = Form.useForm();
  const updateInfo = useUpdateBasicInfos();
  
  const context = useContext(appContext);

  /* const user = useCurrentUser(null, null, true);*/

  const user = context.user;
  const logOut = context.logOut;

  const [nameDisabled, setNameDisabled] = useState(true);
  const [passwordsDisabled, setPasswordsDisabled] = useState(true);
  const [emailDisabled, setEmailDisabled] = useState(true);
  const [phoneDisabled, setPhoneDisabled] = useState(true);

  const onFinish = (values) => {
          console.log(`VALUES: ${JSON.stringify(values)}`)
      updateInfo.mutate(values, {
        onError: (error) =>
          Modal.error({
            title: "Error:",
            content: error.response.data.message,
            onOk: updateInfo.reset(),
          }),

        onSuccess: (data) => {
              logOut.mutate(null, {
                onSuccess: () => {
                  Modal.success({
                    title:
                      "Your info has been updated successfully! Please login again",
                    onOk: () => {
                      logOut.reset();
                    },
                  });
                },
              });
        }, 
      });
  }

  if(user.isLoading || user.data?.data === null){
    return <Skeleton active={true} loading={true}></Skeleton>
  }

  if(user.isError){
    return <Result status={'error'} title='Some errors occurred. Please try again later'></Result>
  }

  return (
    <Spin
      spinning={updateInfo.isLoading}
      tip={"Proccessing..."}
      style={{ width: "100%", height: 300, textAlign: "center" }}
    >
      <Row justify="center">
        <Col xs={0} sm={0} md={3} lg={3} xl={3} xxl={3}></Col>
        <Col xs={24} sm={24} md={18} lg={18} xl={18} xxl={18}>
          <Form
            form={form}
            name="updateInfo"
            /* className="login-form" */
            initialValues={{
              username: null,
              password: null,
              email: null,
              phoneNumber: null,
            }}
            labelCol={{ span: 4 }}
            labelAlign="left"
            wrapperCol={{
              span: 20,
            }}
            onFinish={onFinish}
          >
            {/*User name*/}
            <Form.Item label="User name" name="username">
              <Input
                disabled={nameDisabled}
                placeholder={user.data?.data.username}
                addonBefore={
                  <Button
                    type="link"
                    size="small"
                    style={{ padding: 0 }}
                    onClick={() => setNameDisabled(false)}
                  >
                    Edit
                  </Button>
                }
                addonAfter={
                  <Button
                    type="link"
                    size="small"
                    style={{ padding: 0 }}
                    onClick={() => {
                      form.resetFields(["username"]);
                      setNameDisabled(true);
                    }}
                  >
                    Cancel
                  </Button>
                }
              />
            </Form.Item>

            {/*Password*/}
            <Form.Item name="password" label="Password">
              <Input.Password
                disabled={passwordsDisabled}
                iconRender={(visible) =>
                  visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />
                }
                addonBefore={
                  <Button
                    type="link"
                    size="small"
                    style={{ padding: 0 }}
                    onClick={() => setPasswordsDisabled(false)}
                  >
                    Edit
                  </Button>
                }
                addonAfter={
                  <Button
                    type="link"
                    size="small"
                    style={{ padding: 0 }}
                    onClick={() => {
                      form.resetFields(["password"]);
                      setPasswordsDisabled(true);
                    }}
                  >
                    Cancel
                  </Button>
                }
              />
            </Form.Item>

            {/*Passwords confirm */}
            {/* <Form.Item
              name="confirm"
              label="Confirm Password"
              dependencies={["password"]}
              hasFeedback
              rules={[
                {
                  required: true,
                  message: "Please confirm new password",
                },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    if (!value || getFieldValue("password") === value) {
                      return Promise.resolve();
                    }

                    return Promise.reject(
                      new Error(
                        "The two passwords that you entered do not match"
                      )
                    );
                  },
                }),
              ]}
            >
              <Input.Password disabled={passwordsDisabled} />
            </Form.Item> */}

            {/* Email */}
            <Form.Item
              name="email"
              label="E-mail"
              rules={[
                {
                  type: "email",
                  message: "This is not a valid E-mail",
                },
              ]}
            >
              <Input
                disabled={emailDisabled}
                placeholder={user.data?.data.email}
                addonBefore={
                  <Button
                    type="link"
                    size="small"
                    style={{ padding: 0 }}
                    onClick={() => setEmailDisabled(false)}
                  >
                    Edit
                  </Button>
                }
                addonAfter={
                  <Button
                    type="link"
                    size="small"
                    style={{ padding: 0 }}
                    onClick={() => {
                      form.resetFields(["email"]);
                      setEmailDisabled(true);
                    }}
                  >
                    Cancel
                  </Button>
                }
              />
            </Form.Item>

            {/*Phone number */}
            <Form.Item name="phoneNumber" label="Phone number">
              <Input
                disabled={phoneDisabled}
                placeholder={user.data?.data.phoneNumber}
                style={{
                  width: "100%",
                }}
                addonBefore={
                  <Button
                    type="link"
                    size="small"
                    style={{ padding: 0 }}
                    onClick={() => setPhoneDisabled(false)}
                  >
                    Edit
                  </Button>
                }
                addonAfter={
                  <Button
                    type="link"
                    size="small"
                    style={{ padding: 0 }}
                    onClick={() => {
                      form.resetFields(["phoneNumber"]);
                      setPhoneDisabled(true);
                    }}
                  >
                    Cancel
                  </Button>
                }
              />
            </Form.Item>

            {/*Password*/}
            <Form.Item
              name="currentPasswords"
              label="Current passwords"
              rules={[
                {
                  required: true,
                  message: "Please enter your passwords",
                },
              ]}
              hasFeedback
            >
              <Input.Password
                placeholder="Password"
                disabled={
                  nameDisabled &&
                  passwordsDisabled &&
                  emailDisabled &&
                  phoneDisabled
                }
                iconRender={(visible) =>
                  visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />
                }
                prefix={<LockOutlined className="site-form-item-icon" />}
              />
            </Form.Item>

            <Form.Item>
              <Button
                disabled={
                  nameDisabled &&
                  passwordsDisabled &&
                  emailDisabled &&
                  phoneDisabled
                }
                style={{ width: 80 }}
                type="primary"
                htmlType="submit"
                className="login-form-button"
              >
                Update
              </Button>
            </Form.Item>
          </Form>
        </Col>
        <Col xs={0} sm={0} md={3} lg={3} xl={3} xxl={3}></Col>
      </Row>
    </Spin>
  );
}

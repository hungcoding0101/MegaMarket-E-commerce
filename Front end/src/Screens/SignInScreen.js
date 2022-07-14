import React, { useState } from 'react'
import { Form, Input, Button, Checkbox, Row, Col, Modal } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';
import {useCurrentUser, useLogIn} from '../Hooks/UserHooks'
import '../index.css'
import { EyeInvisibleOutlined, EyeTwoTone } from "@ant-design/icons";
import ResetPasswords from '../components/ResetPasswords';


// eslint-disable-next-line no-undef
export default function SignInScreen({ visible, setVisible, onCancel}) {
  const [signInForm] = Form.useForm();

  const login = useLogIn();
  const user = useCurrentUser(
    (data) => console.log(`FETCHED USER: ${JSON.stringify(data)}`),
    null,
    login.isSuccess
  );

  const [forgetPassVisible, setForgetPassVisible] = useState(false);


  const onFinish = async (values) => {
    console.log("Received values of form: ", values);
/*     console.log(`HERE: PREVIOUS: ${JSON.stringify(props.state)}`); */
    login.mutate(values, {
      onError: (error) =>
        Modal.error({
          title:
            error.response.data.message === "Bad credentials"
              ? "Wrong user name or passwords"
              : "Some error occurred",
        }),

      onSuccess: (data) => {
        signInForm.resetFields();
        setVisible(false);
        Modal.success({
          title: 'Logged in successfully!'
        })
      },
    });
  };

  return (
    <Modal
      title="Sign in"
      visible={visible}
      centered={true}
      onCancel={() => {
        signInForm.resetFields();
        onCancel();
      }}
      width={"100%"}
      style={{ maxWidth: 500 }}
      footer={null/* [
        <Button type="primary" onClick={onCancel}>
          Cancel
        </Button>,
      ] */}
    >
      <Row justify="center">
        <Col span={6}></Col>
        <Col span={12}>
          <Form
            form={signInForm}
            name="normal_login"
            className="login-form"
            style={{textAlign: 'center'}}
            initialValues={{
              rememberme: true,
            }}
            onFinish={onFinish}
          >
            <Form.Item
              name="username"
              rules={[
                {
                  required: true,
                  message: "Please input your Username!",
                },
              ]}
            >
              <Input
                prefix={<UserOutlined className="site-form-item-icon" />}
                placeholder="Username"
              />
            </Form.Item>
            <Form.Item
              name="password"
              rules={[
                {
                  required: true,
                  message: "Please input your Password!",
                },
              ]}
            >
              <Input.Password
                placeholder="input password"
                iconRender={(visible) =>
                  visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />
                }
                prefix={<LockOutlined className="site-form-item-icon" />}
              />
            </Form.Item>
            <Form.Item>
              <Form.Item name="rememberme" valuePropName="checked" noStyle>
                <Checkbox>Remember me</Checkbox>
              </Form.Item>
              {/* 
              <a className="login-form-forgot" href="">
                Forgot password
              </a> */}
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                className="login-form-button"
              >
                Log in
              </Button>
            </Form.Item>
          </Form>
          Or <Link to="/signup">register now!</Link>
          <div>
            <Button
              type="link"
              onClick={() => setForgetPassVisible(true)}
              style={{ padding: 0 }}
              danger
            >
              Forgot your passwords?
            </Button>
          </div>
        </Col>
        <Col span={6}></Col>
      </Row>
      <ResetPasswords
        onCancel={() => setForgetPassVisible(false)}
        visible={forgetPassVisible}
      ></ResetPasswords>
    </Modal>
  );
}
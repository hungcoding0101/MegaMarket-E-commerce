import React from 'react'
import { Form, Input, Button, Checkbox, Row, Col, Radio, Modal, Spin } from 'antd';
import {
  UserOutlined,
  LockOutlined,
  MailOutlined,
  PhoneOutlined,
  EyeTwoTone,
  EyeInvisibleOutlined,
} from "@ant-design/icons";
import { Redirect } from 'react-router-dom';
import {typeOfSignUP} from '../constants/UserConstant'
import '../index.css'
import { useCurrentUser, useLogIn, useSignUp } from '../Hooks/UserHooks';
export default function SignUpScreen(props) {

     const login = useLogIn();

     const user = useCurrentUser(
       (data) => console.log(`FETCHED USER: ${JSON.stringify(data)}`),
       null,
       login.isSuccess
     );

     const signup = useSignUp();

    async function onFinish (values)  {
        console.log('Received values of form: ', values);
        signup.mutate(values, {
          onError: (error) =>
            Modal.error({
              title: "Error:",
              content: error.response.data.message,
            }),
          
          onSuccess: (data) => {
            Modal.success({
              title:"Your account has been created!",
            })

            login.mutate({
              username: values.username,
              password: values.password,
              rememberme: values.rememberme,
            });
          }
        });
    };

    if (user.isSuccess) {
       return (
         <Redirect
           to={'/'}
         ></Redirect>
       );
    }

  return (
    <Spin
      spinning={signup.isLoading}
      tip={"Proccessing..."}
      style={{ width: "100%", height: 300, textAlign: "center" }}
    >
      <Row justify="center">
        <Col xs={0} sm={0} md={8} lg={8} xl={8} xxl={8}></Col>
        <Col xs={24} sm={24} md={8} lg={8} xl={8} xxl={8}>
          <Form
            name="normal_login"
            className="login-form"
            initialValues={{
              remember: true,
            }}
            labelCol={{ span: 10 }}
            labelAlign="left"
            wrapperCol={{
              span: 14,
            }}
            onFinish={onFinish}
          >
            {/*Sign up type */}
            <Form.Item
              label="Sign up as:"
              name={"role"}
              rules={[
                { required: true, message: "Please choose a sign up type" },
              ]}
            > 
              <Radio.Group buttonStyle="solid">
                <Radio.Button value={typeOfSignUP.customer}>
                  Customer
                </Radio.Button>
                <Radio.Button value={typeOfSignUP.seller}>Seller</Radio.Button>
              </Radio.Group>
            </Form.Item>

            {/*User name*/}
            <Form.Item
              name="username"
              label="User name"
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

            {/*Password*/}
            <Form.Item
              name="password"
              label="Password"
              rules={[
                {
                  required: true,
                  message: "Please input your Password!",
                },
              ]}
              hasFeedback
            >
              <Input.Password
                placeholder="Password"
                iconRender={(visible) =>
                  visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />
                }
                prefix={<LockOutlined className="site-form-item-icon" />}
              />
            </Form.Item>

            {/*Passwords confirm */}
            <Form.Item
              name="confirm"
              label="Confirm Password"
              dependencies={["password"]}
              hasFeedback
              rules={[
                {
                  required: true,
                  message: "Please confirm your password!",
                },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    if (!value || getFieldValue("password") === value) {
                      return Promise.resolve();
                    }

                    return Promise.reject(
                      new Error(
                        "The two passwords that you entered do not match!"
                      )
                    );
                  },
                }),
              ]}
            >
              <Input.Password />
            </Form.Item>

            {/* Email */}
            <Form.Item
              name="email"
              label="E-mail"
              rules={[
                {
                  type: "email",
                  message: "This is not a valid E-mail",
                },
                {
                  required: true,
                  message: "Please provide your E-mail",
                },
              ]}
            >
              <Input
                prefix={<MailOutlined className="site-form-item-icon" />}
              />
            </Form.Item>

            {/*Phone number */}
            <Form.Item
              name="phoneNumber"
              label="Phone number"
              rules={[
                {
                  required: true,
                  message: "Please provide a phone number",
                },
              ]}
            >
              <Input
                style={{
                  width: "100%",
                }}
                prefix={
                  <PhoneOutlined className="site-form-item-icon"></PhoneOutlined>
                }
              />
            </Form.Item>

            <Form.Item>
              <Form.Item name="rememberme" valuePropName="checked" noStyle>
                <Checkbox>Remember me</Checkbox>
              </Form.Item>
            </Form.Item>

            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                className="login-form-button"
              >
                Sign up
              </Button>
            </Form.Item>
          </Form>
        </Col>
        <Col xs={0} sm={0} md={8} lg={8} xl={8} xxl={8}></Col>
      </Row>
    </Spin>
  );
}

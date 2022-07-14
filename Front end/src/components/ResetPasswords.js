import React, { useState } from "react";
import { Form, Input, Modal, Result, Steps, Button, Spin, Row, Col} from "antd";
import { useRequestResetCode, useResetPasswords } from "../Hooks/UserHooks";
import {
  MailOutlined,
  EyeTwoTone,
  EyeInvisibleOutlined,
} from "@ant-design/icons";

const ResetPasswords = ({ visible, onCancel }) => {

    const requestCode = useRequestResetCode();
    const resetPasswords = useResetPasswords();

    const requestCodeFinish = (values) => {
        requestCode.mutate(values.email, {
          onError: (error) => Modal.error({title:`Error: ${error.response.data.message}` }),
          onSuccess: (data) => setCurrent(1),
        });
    }

     const resetPasswordsFinish = (values) => {
       resetPasswords.mutate(values, {
        onError: (error) => Modal.error({title:`Error: ${error.response.data.message}` }),
          onSuccess: (data) => setCurrent(2),
       });
     };

     const [current, setCurrent] = useState(0);
     const [emailForm] = Form.useForm();
     const [resetPassForm] = Form.useForm();

    const steps = [
      {
        title: "Get reset code",
        content: (
          <Spin
            spinning={requestCode.isLoading}
            tip={"Uploading your product..."}
          >
            <Form form={emailForm} onFinish={requestCodeFinish}>
              <Form.Item
                name={"email"}
                label={"Your email:"}
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
              <Button
                type="primary"
                htmlType="submit"
                className="login-form-button"
              >
                Send
              </Button>
            </Form>
          </Spin>
        ),
      },
      {
        title: "Reset your passwords",
        content: (
          <Spin
            spinning={resetPasswords.isLoading}
            tip={"Processing..."}
          >
            <h1>
              A code has been sent to your email. Use that code to reset
              your passwords
            </h1>
            <Form
              form={resetPassForm}
              labelCol={{ span: 11 }}
              wrapperCol={{span: 13}}
              onFinish={resetPasswordsFinish}
            >
              <Form.Item
                name={"code"}
                label={"Code"}
                rules={[
                  {
                    required: true,
                    message: "Please provide the code",
                  },
                ]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="newPasswords"
                label="New password"
                rules={[
                  {
                    required: true,
                    message: "Please input new password",
                  },
                ]}
                hasFeedback
              >
                <Input.Password
                  placeholder="Password"
                  iconRender={(visible) =>
                    visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />
                  }
                 
                />
              </Form.Item>

              {/*Passwords confirm */}
              <Form.Item
                name="confirm"
                label="Confirm new password"
                dependencies={["password"]}
                hasFeedback
                rules={[
                  {
                    required: true,
                    message: "Please confirm new password",
                  },
                  ({ getFieldValue }) => ({
                    validator(_, value) {
                      if (!value || getFieldValue("newPasswords") === value) {
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
              <div style={{ width: 200 }}>
                <Button
                  type="primary"
                  htmlType="submit"
                  className="login-form-button"
                >
                  Send
                </Button>
              </div>
            </Form>
          </Spin>
        ),
      },
      {
        title: "Success",
        content: (
          <Result
            status={"success"}
            title="Your passwords has been reset!"
          ></Result>
        ),
      },
    ];
    return (
      <Modal
        title="Reset passwords"
        visible={visible}
        onCancel={() => {
          emailForm.resetFields();
          resetPassForm.resetFields();
          setCurrent(0);
          onCancel();
        }}
        centered={true}
        width={"100%"}
        style={{ maxWidth: 600 }}
        footer={null}
      >
        <Steps current={current}>
          {steps.map((item) => (
            <Steps.Step key={item.title} title={item.title} />
          ))}
        </Steps>
        <Row justify="center">
          <Col span={4}></Col>
          <Col span={16}>
            <div className="steps-content">{steps[current].content}</div>
          </Col>
          <Col span={4}></Col>
        </Row>
      </Modal>
    );
}

export default ResetPasswords;
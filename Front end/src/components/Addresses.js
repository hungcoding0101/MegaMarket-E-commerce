import React, { useEffect, useState } from 'react'
import { useCurrentUser } from '../Hooks/UserHooks';
import { Skeleton, Result, Button, List, Form, Input, Space, Modal } from "antd";
import { PlusOutlined, MinusCircleOutlined, } from "@ant-design/icons";
import { useUpdateAddresses } from '../Hooks/CustomerHooks';

 const  Addresses = () => {

  const user = useCurrentUser(null, null, true);

  const updateAddresses = useUpdateAddresses();

  const [oldAddresses, setOldAddresses] = useState(
    user.data?.data?.deliveryAddresses
  );

  useEffect(
    () => setOldAddresses(user.data?.data?.deliveryAddresses),
    [user.data?.data?.deliveryAddresses]
  );

  const handleUpdate = (values) => {
      const newAddresses = values.newAddresses.map(
        (element) => element.address
      );
      const allAddresses = oldAddresses.concat(newAddresses);

      console.log(`OLD: ${JSON.stringify(oldAddresses)}`)
      console.log(`NEW: ${JSON.stringify(newAddresses)}`);
      console.log(`ALL: ${JSON.stringify(allAddresses)}`);

      updateAddresses.mutate(allAddresses, {
        onError: (error) => {
          Modal.error({ title: error.response.data.message });
        },
        onSuccess: () => {
          Modal.success({
            title: "Updated",
          });
        },
      });

      form.resetFields(["newAddresses"]);
  }

  const [form] = Form.useForm();

  if (user.isError) {
    return (
      <Result
        status={"error"}
        title="Some errors occurred. Please try again later"
      ></Result>
    );
  }

  return (
    <Skeleton active={true} loading={user.isLoading || user.data?.data == null}>
      <List
        dataSource={oldAddresses}
        style={{backgroundColor: 'white'}}
        header={<span style={{ fontWeight: "bold" }}>Current addresses</span>}
        bordered
        renderItem={(item) => (
          <List.Item
            actions={[
              <Button
                type="primary"
                onClick={() =>
                  setOldAddresses(
                    (old) => old.filter((address) => address !== item)
                  )
                }
              >
                Delete
              </Button>,
            ]}
          >
            <div style={{ wordWrap: "break-word", wordBreak: "break-word" }}>
              {item}
            </div>
          </List.Item>
        )}
      ></List>

      <Form
        form={form}
        onFinish={handleUpdate}
        layout="vertical"
        initialValues={{ newAddresses: [] }}
        style={{ textAlign: "center", marginTop: 50 }}
      >
        <Form.List name={"newAddresses"}>
          {(fields, { add, remove }) => (
            <Space direction="vertical">
              {fields.map(({ key, name, ...restField }) => (
                <Space key={key} align="baseline">
                  <Form.Item
                    {...restField}
                    validateTrigger={["onChange", "onBlur"]}
                    name={[name, "address"]}
                    rules={[
                      {
                        required: true,
                        whitespace: true,
                        message: "Please new address or delete this field.",
                      },
                    ]}
                  >
                    <Input style={{ width: 600 }}></Input>
                  </Form.Item>
                  <MinusCircleOutlined
                    onClick={() => {
                      remove(name);
                    }}
                  />
                </Space>
              ))}
              <Form.Item>
                <Button
                  type="primary"
                  onClick={() => add()}
                  icon={<PlusOutlined />} /* style={{ width:"45%"}} */
                >
                  Add new addresses
                </Button>
              </Form.Item>
            </Space>
          )}
        </Form.List>
        <Form.Item>
          <Button
            type="primary"
            danger
            htmlType="submit"
            /* disabled={form.getFieldsValue(true).newAddresses.length === 0} */
          >
            Update
          </Button>
        </Form.Item>
      </Form>
    </Skeleton>
  );
}

export default Addresses;

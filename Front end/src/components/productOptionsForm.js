import { Form, Input, InputNumber, Modal, Button, Space, Upload, Divider } from 'antd';
import {
  MinusCircleOutlined,
  PlusOutlined,
  UploadOutlined,
} from "@ant-design/icons";
import { useEffect, useRef, useState } from 'react';
import {Formatter} from '../Util/Formatter'


export const useResetFormOnCloseModal = ({ form, visible }) => {
  const prevVisibleRef = useRef();

  useEffect(() => {
    prevVisibleRef.current = visible;
  }, [visible]);
  const prevVisible = prevVisibleRef.current;

  useEffect(() => {
    if (!visible && prevVisible) {
      form.resetFields();
    }
  }, [visible]);
};

const ProductOptionForm = ({ visible, onCancel, totalStock }) => {
  const [form] = Form.useForm();

  useResetFormOnCloseModal({
    form,
    visible,
  });

  const onOk = async () => {
    form.submit();
  };

  return (
    <Modal
      title="Add options"
      visible={visible}
      onOk={onOk}
      onCancel={onCancel}
      centered={true}
      width={"100%"}
    >
      <div style={{ color: "red", fontWeight: "bold" }}>
        {`Total stock: ${totalStock}`}
      </div>

      <Form
        form={form}
        layout="vertical"
        validateTrigger="onsubmit"
        name="productOptionsForm"
        initialValues={{ optionImages: [] }}
      >
        <Form.Item
          name="characteristic"
          label="Characteristic"
          tooltip={'Ex: "màu sắc", "chất liệu",...'}
          rules={[
            {
              required: true,
            },
          ]}
        >
          <Input />
        </Form.Item>

        {/*Option objects */}
        <Form.List
          name="options"
          rules={[
            {
              validator: async (_, values) => {
                if (!values || values.length < 1) {
                  return Promise.reject(new Error("Must add choices"));
                }
                if (
                  values
                    .map((option) => option.stock)
                    .reduce(
                      (previousValue, currentValue) =>
                        previousValue + currentValue,
                      0
                    ) !== totalStock
                ) {
                  return Promise.reject(
                    new Error(
                      `Sum of all options's stock must match total stock: ${totalStock}`
                    )
                  );
                }
              },
            },
          ]}
        >
          {(fields, { add, remove }, { errors }) => (
            <>
              {fields.map(({ key, name, ...restField }) => (
                <>
                  <Divider>New option</Divider>
                  <Space
                    key={key}
                    style={{ display: "flex", marginBottom: 8 }}
                    align="baseline"
                    wrap={true}
                  >
                    <Form.Item
                      label="Option Name"
                      {...restField}
                      name={[name, "name"]}
                      tooltip={
                        'Ex: "xanh", "đỏ", "có đường", "không đường",...'
                      }
                      rules={[
                        {
                          required: true,
                          message: "Provide choice name or delete this field",
                        },
                      ]}
                    >
                      <Input placeholder="Option name" />
                    </Form.Item>

                    <Form.Item
                      label="Price difference"
                      {...restField}
                      name={[name, "priceDifference"]}
                      initialValue={0}
                      tooltip={
                        "The additional price you want to add to this option"
                      }
                      rules={[
                        {
                          validator: async (_, values) => {
                            if (values < 0 || values % 1000 !== 0) {
                              return Promise.reject(
                                new Error(
                                  "The price must be a multiple of 1000"
                                )
                              );
                            }
                          },
                        },
                      ]}
                    >
                      <InputNumber
                        step="1000"
                        addonAfter="vnd"
                        /* defaultValue={0} */
                        formatter={(value) =>
                          Formatter.addNumberSeparator(value)
                        }
                        parser={(value) => Formatter.getNumberRawValue(value)}
                      />
                    </Form.Item>

                    <Form.Item
                      label={"Stock"}
                      {...restField}
                      name={[name, "stock"]}
                      rules={[
                        {
                          required: true,
                          message: "Please provide stock for this choice",
                        },
                      ]}
                    >
                      <InputNumber
                        min="0"
                        step="1"
                        addonAfter="unit"
                        formatter={(value) =>
                          Formatter.addNumberSeparator(value)
                        }
                        parser={(value) => Formatter.getNumberRawValue(value)}
                      />
                    </Form.Item>

                    <Form.Item
                      {...restField}
                      name={[name, "imageUrl"]}
                      label="image"
                      getValueFromEvent={(e) => {
                        const thisFile = e.file;

                        const newName =
                          thisFile.uid +
                          thisFile.name.substring(
                            thisFile.name.lastIndexOf(".")
                          );

                        /*Create a new file with name based on uid*/
                        const newFile = new File([thisFile], newName, {
                          type: thisFile.type,
                        });

                        /*Add the new file to uploaded file list*/
                        const optionImages =
                          form.getFieldValue("optionImages") || [];
                        form.setFieldsValue({
                          optionImages: [...optionImages, newFile],
                        });

                        /*Set the new file name as value of this Form item */
                        return newName;
                      }}
                      preserve={false}
                    >
                      <Upload
                        name="file"
                        listType="picture"
                        accept="image/*"
                        maxCount={1}
                        onRemove={() =>
                          form.setFieldsValue({ image: undefined })
                        }
                        beforeUpload={(file) => {
                          return false;
                        }}
                      >
                        <Button icon={<UploadOutlined />} type={"primary"}>
                          Upload
                        </Button>
                      </Upload>
                    </Form.Item>

                    <MinusCircleOutlined onClick={() => remove(name)} />
                  </Space>
                </>
              ))}
              <Form.Item>
                <Form.ErrorList errors={errors} />
                <Button
                  style={{ marginTop: 20 }}
                  type="primary"
                  onClick={() => add()}
                  icon={<PlusOutlined />} /* style={{ width:"45%"}} */
                  block
                >
                  Add choice
                </Button>
              </Form.Item>
            </>
          )}
        </Form.List>
        <Form.Item name={"optionImages"} hidden={true}></Form.Item>
      </Form>
    </Modal>
  );
};

export default ProductOptionForm;

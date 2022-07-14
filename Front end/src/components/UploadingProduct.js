import { useEffect, useState } from 'react';
import { Form, Input, Button, Space, InputNumber, Upload, Row, Col, List, Divider, Modal, Spin, Cascader } from 'antd';
import { UploadOutlined, MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import 'antd/dist/antd.css';
import Text from 'antd/lib/typography/Text';
import ProductOptionForm from './productOptionsForm';
import { Formatter } from '../Util/Formatter';
import { useProductCategoryTree } from "../Hooks/ProductCategoryHooks";
import { useUploadProduct } from '../Hooks/SellerHooks';
import { useWatch } from 'antd/lib/form/Form';


export default function UploadingProduct (props){

    const productCategoryTree = useProductCategoryTree(null, null, true);

    const upload = useUploadProduct();

    const [avatarState, setAvatarState] = useState({});
    let [otherImageState, setOtherImageState] = useState([]);
    const [addedOptions, setAddedOptions] = useState([]);
    const data = productCategoryTree?.data?.data; 
    const [thisForm] = Form.useForm();
    const totalStockWatch = Form.useWatch("totalStock", thisForm);

    const onFinish = () => {
        let formData = new FormData();
        const values = thisForm.getFieldsValue(true);

        delete values.avatarImage;
        delete values.otherImages;
        
        formData.append('avatarImage', avatarState);

        for(const file of otherImageState){
          formData.append('otherImages', file);
        }

        let categories = values.categories;
        let set = new Set(categories.flat());
        values.categories = new Array(...set);

        let specificDetails = values.specificDetails;
        if(Array.isArray(specificDetails) && specificDetails.length){
                  const mappedDetails = new Map();
                  specificDetails.forEach((detail) => {
                    mappedDetails.set(detail.feature, detail.description);
                  });
                  values.specificDetails = Object.fromEntries(mappedDetails);
        }

        let paymentMethods = values.paymentMethods;
        if (Array.isArray(paymentMethods) && paymentMethods.length) {
          const methodList = paymentMethods.map((method) => method.methodName);
          values.paymentMethods = methodList;
        }
        

        const productOptions = values.productOptions;
        values.characteristics = [];
        if(Array.isArray(productOptions) && productOptions.length){
            const newOptions = [];
            for(const element of productOptions){
                for(const option of element.options){
                    option.characteristic = element.characteristic;
                    newOptions.push(option);
                }

                if(element.optionImages.length){
                    for(const imageFile of element.optionImages){
                      formData.append("optionImages", imageFile);
                    }
                }

                values.characteristics.push(element.characteristic);
            }
            values.productOptions = newOptions;
        } 

        const stringValues = JSON.stringify(values)
        formData.append("product",stringValues)

        console.log(`Received values of form: ${JSON.stringify(values)} || ${avatarState} ||${otherImageState}`);

        
        upload.mutate(formData, {
            onError: (error) =>
              Modal.error({
                title: `Error: ${error.response.data.message}`,
                onOk: () => upload.reset(),
              }),
            onSuccess: () =>
              Modal.success({
                title: "Uploaded successfully!",
                onOk: () => upload.reset(),
              }),
        });

        thisForm.resetFields([
          "specificDetails",
          "avatarImage",
          "otherImages",
          "totalStock",
        ]);
        setAddedOptions([]);
    }

    const normFile = (e) => {
      if (Array.isArray(e)) {
        return e;
      }

      return e && e.fileList;
    };

    const deleteVariety = (item) => {
      setAddedOptions(addedOptions.filter((option) => option !== item))
    };

    const [visible, setVisible] = useState(false);

    useEffect(() => thisForm.setFieldsValue({productOptions: addedOptions}), [addedOptions])

    return (
      <Spin spinning={upload.isLoading} tip={"Uploading your product..."}>
        <Form.Provider
          onFormFinish={(name, { values, forms }) => {
            if (name === "productOptionsForm") {
              setAddedOptions((old) => [...old, values]);
              setVisible(false);
              console.log(
                `HERE: VARIETY: ${JSON.stringify(
                  thisForm.getFieldValue("productOptions")
                )}`
              );
            }
          }}
        >
          <Form
            name="product_uploading"
            onFinish={onFinish}
            autoComplete="off"
            id="form"
            layout="vertical"
            style={{ textAlign: "center" }}
            form={thisForm}
            scrollToFirstError
            initialValues={{
              deliveryFeePerUnit: 0,
              maxDeliveryFee: 0,
              totalStock: undefined,
              defaultUnitPrice: undefined,
              productOptions: addedOptions,
            }}
          >
            <Row justify="center">
              <Col span={4}></Col>
              <Col span={16}>
                {/*name*/}
                <Form.Item
                  name={"name"}
                  label="Product name:"
                  rules={[
                    {
                      required: true,
                      message: "Please provide a product name",
                    },
                    {
                      pattern: "[a-zA-Z0-9]+",
                      message: "Must be letters or numbers",
                    },
                  ]}
                >
                  <Input
                    type={Text}
                    style={{ display: "flex", marginBottom: 8 }}
                  ></Input>
                </Form.Item>

                {/*categories*/}
                <Form.Item
                  name={"categories"}
                  rules={[
                    {
                      required: true,
                      message: "Please choose at least 1 category",
                    },
                  ]}
                  label="Product categories"
                >
                  <Cascader
                    showCheckedStrategy="SHOW_CHILD"
                    fieldNames={{
                      label: "title",
                      value: "value",
                      children: "children",
                    }}
                    options={data}
                    dropdownClassName="categoryDropdown"
                    dropdownMatchSelectWidth={false}
                    allowClear={true}
                    maxTagCount="responsive"
                    placeholder="Choose a category"
                    expandTrigger={"hover"}
                    onChange={(value, selectedOptions) =>
                      value.map((choice) => console.log(choice))
                    }
                    label="Choose product category"
                  />
                </Form.Item>

                {/*unit*/}
                <Form.Item
                  name={"unit"}
                  label="Product unit:"
                  rules={[
                    {
                      required: true,
                      message: "Please provide unit for this product",
                    },
                    {
                      pattern: "[a-zA-Z0-9]+",
                      message: "Must be letters or numbers",
                    },
                  ]} /* style={{width: '30%'}} */
                >
                  <Input
                    type={Text}
                    style={{ display: "flex", marginBottom: 8 }}
                  ></Input>
                </Form.Item>

                {/*unitPrice*/}
                <Form.Item
                  name={"defaultUnitPrice"}
                  label={"Default Price"}
                  rules={[
                    {
                      required: true,
                      message: "Please provide Default Price",
                    },
                    {
                      validator: async (_, values) => {
                        if (values < 0 || values % 1000 !== 0) {
                          return Promise.reject(
                            new Error("The price must be a multiple of 1000")
                          );
                        }
                      },
                    },
                  ]}
                >
                  <InputNumber
                    min="1000"
                    step="1000"
                    addonAfter="vnd"
                    formatter={(value) => Formatter.addNumberSeparator(value)}
                    parser={(value) => Formatter.getNumberRawValue(value)}
                  />
                </Form.Item>

                {/*stock*/}
                <Form.Item
                  name={"totalStock"}
                  label={"Total stock"}
                  rules={[
                    {
                      required: true,
                      message: "Please provide Total price",
                    },
                  ]}
                >
                  <InputNumber
                    min="1"
                    step="1"
                    onChange={(value) => {
                      console.log(`TOTAL STOCK: ${value}`);
                      setAddedOptions([]);
                      /*                    if (value) {
                        setDisableVariety(false);
                      } else {
                        setDisableVariety(true);
                      } */
                    }}
                    addonAfter="unit"
                    formatter={(value) => Formatter.addNumberSeparator(value)}
                    parser={(value) => Formatter.getNumberRawValue(value)}
                  />
                </Form.Item>

                {/*productOptions*/}
                <Form.Item>
                  <Button
                    type="primary"
                    icon={<PlusOutlined />}
                    disabled={totalStockWatch == null}
                    onClick={() => setVisible(true)}
                    block
                  >
                    Add Options
                  </Button>
                </Form.Item>

                {/*List of created Variety */}
                <Form.Item>
                  <List
                    bordered={true}
                    style={{ backgroundColor: "white" }}
                    dataSource={addedOptions}
                    size="small"
                    rowKey={(item) => addedOptions.indexOf(item)}
                    renderItem={(item) => (
                      <List.Item
                        actions={[
                          <Button
                            type="text"
                            danger
                            onClick={() => deleteVariety(item)}
                          >
                            Delete
                          </Button>,
                        ]}
                      >
                        <List.Item.Meta
                          title={item.characteristic}
                          description={
                            <ul>
                              {item.options.map((option, index) => (
                                <li key={index}>
                                  <div style={{ color: "black" }}>
                                    {option.name}
                                    {<Divider type="vertical"></Divider>} Price
                                    difference: {option.priceDifference}
                                    {
                                      <Divider type="vertical"></Divider>
                                    } stock: {option.stock}
                                  </div>
                                </li>
                              ))}
                            </ul>
                          }
                        />
                      </List.Item>
                    )}
                  ></List>
                </Form.Item>

                {/*estimatedDeliveryTime*/}
                <Form.Item
                  name={"estimatedDeliveryTime"}
                  label="Estimated delivery time:"
                  rules={[
                    {
                      required: true,
                      message: "Please provide estimated delivery time",
                    },
                    {
                      pattern: "[a-zA-Z0-9]+",
                      message: "Must be letters or numbers",
                    },
                  ]} /* style={{width: '30%'}} */
                >
                  <Input
                    type={Text}
                    style={{ display: "flex", marginBottom: 8 }}
                  ></Input>
                </Form.Item>

                {/*deliveryFeePerUnit*/}
                <Form.Item
                  name={"deliveryFeePerUnit"}
                  label={"Delivery fee per unit"}
                  rules={[]}
                >
                  <InputNumber
                    /* style={{width: '30%'}} */
                    min="0"
                    step="1000"
                    addonAfter="vnd"
                    formatter={(value) => Formatter.addNumberSeparator(value)}
                    parser={(value) => Formatter.getNumberRawValue(value)}
                  />
                </Form.Item>

                {/*maxDeliveryFee*/}
                <Form.Item
                  name={"maxDeliveryFee"}
                  label={"Max delivery fee"}
                  rules={[]}
                >
                  <InputNumber
                    /* style={{width: '30%'}} */
                    defaultValue="0"
                    min="0"
                    step="1000"
                    addonAfter="vnd"
                    formatter={(value) => Formatter.addNumberSeparator(value)}
                    parser={(value) => Formatter.getNumberRawValue(value)}
                  />
                </Form.Item>

                {/*----------------------------------------------------- */}
                {/*paymentMethods*/}
                <Form.List name="paymentMethods">
                  {(fields, { add, remove }) => (
                    <>
                      {fields.map(({ key, name, ...restField }) => (
                        <Space
                          key={key}
                          style={{ display: "flex", marginBottom: 8 }}
                          align="baseline"
                        >
                          <Form.Item
                            label="Method:"
                            {...restField}
                            name={[name, "methodName"]}
                            rules={[
                              {
                                required: true,
                                message:
                                  "Provide the payment method name or delete this field",
                              },
                              {
                                pattern: "[a-zA-Z0-9]+",
                                message: "Must be letters or numbers",
                              },
                            ]}
                          >
                            <Input placeholder="Method name" />
                          </Form.Item>

                          <MinusCircleOutlined onClick={() => remove(name)} />
                        </Space>
                      ))}
                      <Form.Item>
                        <Button
                          type="primary"
                          onClick={() => add()}
                          block
                          icon={<PlusOutlined />} /* style={{ width:"45%"}} */
                        >
                          Add Payment method
                        </Button>
                      </Form.Item>
                    </>
                  )}
                </Form.List>

                {/*------------------------------------------------------- */}

                {/*brand*/}
                <Form.Item
                  name={"brand"}
                  label="Brand:"
                  rules={[
                    {
                      pattern: "[a-zA-Z0-9]+",
                      message: "Must be letters or numbers",
                    },
                  ]} /* style={{width: '30%'}} */
                >
                  <Input
                    type={Text}
                    style={{ display: "flex", marginBottom: 8 }}
                  ></Input>
                </Form.Item>

                {/*specificDetails*/}
                <Form.List name="specificDetails">
                  {(fields, { add, remove }) => (
                    <>
                      {fields.map(({ key, name, ...restField }) => (
                        <Space
                          key={key}
                          style={{ display: "flex", marginBottom: 8 }}
                          align="baseline"
                        >
                          <Form.Item
                            label="Feature"
                            {...restField}
                            name={[name, "feature"]}
                            rules={[
                              {
                                required: true,
                                message:
                                  "Provide detail name or delete this field",
                              },
                              {
                                pattern: "[a-zA-Z0-9]+",
                                message: "Must be letters or numbers",
                              },
                            ]}
                          >
                            <Input placeholder="Feature" />
                          </Form.Item>

                          <Form.Item
                            label="Description"
                            {...restField}
                            name={[name, "description"]}
                            rules={[
                              {
                                required: true,
                                message:
                                  "Provide detail description or delete this field",
                              },
                              {
                                pattern: "[a-zA-Z0-9]+",
                                message: "Must be letters or numbers",
                              },
                            ]}
                          >
                            <textarea placeholder="description"></textarea>
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
                          block
                          icon={<PlusOutlined />} /* style={{ width:"45%"}} */
                        >
                          Add product details
                        </Button>
                      </Form.Item>
                    </>
                  )}
                </Form.List>

                {/*Description*/}
                <Form.Item
                  name={"productDescription"}
                  label="General description:" /* style={{width: '30%'}} */
                >
                  <textarea
                    type={Text}
                    style={{ display: "flex", marginBottom: 8 }}
                    autoSize={true}
                  ></textarea>
                </Form.Item>

                {/*Upload avatar image*/}
                <Form.Item
                  name="avatarImage"
                  label="Avatar image for the product"
                  valuePropName="fileList"
                  getValueFromEvent={normFile}
                  preserve={false}
                  tooltip={"You can choose only 1 avatar"}
                >
                  <Upload
                    name="avatar"
                    listType="picture"
                    accept="image/*"
                    maxCount={1}
                    onRemove={() => setAvatarState({})}
                    beforeUpload={(file) => {
                      return false;
                    }}
                    onChange={(e) => setAvatarState(e.file)}
                  >
                    <Button icon={<UploadOutlined />}>Click to upload</Button>
                  </Upload>
                </Form.Item>

                {/*Upload other images*/}
                <Form.Item
                  name="otherImages"
                  label="Other images"
                  valuePropName="fileList"
                  getValueFromEvent={normFile}
                  preserve={false}
                >
                  <Upload
                    name="other"
                    listType="picture"
                    accept="image/*"
                    multiple
                    maxCount={10}
                    onRemove={(file) => {
                      const index = otherImageState.indexOf(file);
                      otherImageState.splice(index, 1);
                    }}
                    beforeUpload={(file) => {
                      return false;
                    }}
                    onChange={(e) => {
                      otherImageState.push(e.file);
                    }}
                  >
                    <Button icon={<UploadOutlined />}>
                      Click to upload (Max 10 pics)
                    </Button>
                  </Upload>
                </Form.Item>

                <Form.Item>
                  <Button type="primary" htmlType="submit">
                    Submit
                  </Button>
                </Form.Item>
              </Col>
              <Col span={4}></Col>
            </Row>
          </Form>

          <ProductOptionForm
            visible={visible}
            onCancel={() => setVisible(false)}
            totalStock={thisForm.getFieldValue("totalStock")}
          ></ProductOptionForm>
        </Form.Provider>
      </Spin>
    );
}
 
import React, { useContext, useEffect, useRef, useState } from 'react'
import {Link} from 'react-router-dom'
import { Button, Card, Carousel, Col, Divider, Form, Image,
   InputNumber, Rate, Row, Modal, Space, Result, Spin, Badge, Descriptions, Empty, List, Skeleton } from 'antd';
import {
CheckOutlined, UserOutlined
} from "@ant-design/icons";   
import { Formatter } from '../Util/Formatter';
import { Redirect } from 'react-router-dom';
import {useCurrentUser } from '../Hooks/UserHooks';
import { useOneProduct } from '../Hooks/ProductHooks';
import { useAddToCart } from "../Hooks/CustomerHooks";
import { appContext } from '../App';

export default function ProductScreen(props) {
      
      const context = useContext(appContext);

      const setSignInVisible = context.setSignInVisible;

      const fetchProduct = useOneProduct(props.match.params.id, null, null, true);

      const fetchUser = useCurrentUser(null, null, false);

      const addToCart = useAddToCart();

      /* const [product, setProduct] = useState(fetchProduct?.data); */

      const [focusedImage, setFocusedImage] = useState(null);

      const [selectedOptions, setSelectedOptions] = useState([]);

      const [currentPrice, setCurrentPrice] = useState(null);

      const { Meta } = Card;

      useEffect(() => console.log(
                                  `SELECTED: ${JSON.stringify(selectedOptions)}`
                                ), [selectedOptions])

      const input = useRef();

/*       if (fetchProduct.isLoading || fetchProduct.data.data == null) {
        return (
          <div style={{ width: "100%", height: 300, textAlign: "center" }}>
            <Spin tip="Loading..."></Spin>
          </div>
        );
      } */

      if (fetchProduct.isError) {
        return (
          <Result
            status={"404"}
            title="Not found"
            subTitle="Sorry, the product you searched does not exist."
            extra={
              <Link to={"/"}>
                <Button type="primary">Back Home</Button>
              </Link>
            }
          />
        );
      }

      if(fetchProduct.isSuccess){
        const multipleChoices =
          Array.isArray(fetchProduct?.data?.data?.productOptions) &&
          fetchProduct?.data?.data?.productOptions.length > 0;

        const stock = fetchProduct?.data?.data?.totalStock;

        const addCart = (values) => {
          if(!fetchUser.isSuccess || fetchUser.data.data == null){
          Modal.error({
            title: "Please log in first!",
            onOk: () => setSignInVisible(true),
          });
          return;
        }
          const cartItem = {
            productId: fetchProduct?.data.data.id,
            productVersion: fetchProduct?.data.data.version,
            quantity: values.quantity,
          };

          if(multipleChoices){
            if (selectedOptions.length < fetchProduct?.data?.data?.characteristics.length){
                 Modal.error({
                   title: `Please choose ${fetchProduct?.data?.data?.characteristics.join(
                     ", "
                   )}`,
                 });
                 return;
            }
             
            else{
                const choiceIds = selectedOptions.map((option) => option.id);
                console.log(`OPTIONS ID: ${JSON.stringify(choiceIds)}`);
                cartItem.choiceIds = Array.from(new Set(choiceIds));
                console.log(`CART: ${JSON.stringify(cartItem)}`);
            }
          } 
          addToCart.mutate(cartItem, {
            onError: (error) =>
              Modal.error({ title: `Error: ${error.response.data.message}` }),
            onSuccess: () =>
              Modal.success({ title: "Added this item to your cart" }),
          });
        };

        const optionBorder = (option) => {
          return {
            border: `0.2rem solid ${
              selectedOptions.includes(option)
                ? `rgb(28, 25, 227)`
                : `rgb(255, 255, 255)`
            }`,
          };
        }

        return (
          <Skeleton
            active={true}
            loading={fetchProduct.isLoading || fetchProduct.data.data == null}
          >
            <div>
              <Row>
                <Col
                  xs={24}
                  sm={24}
                  md={8}
                  lg={8}
                  xl={8}
                  xxl={8}
                  style={{ padding: 20 }}
                >
                  <div className="focusedImageContainer">
                    <img
                      className="product_img"
                      alt=""
                      src={
                        focusedImage
                          ? focusedImage
                          : fetchProduct.data.data.avatarImageURL
                      }
                    ></img>
                  </div>

                  <Carousel
                    draggable={true}
                    slidesToScroll={4}
                    slidesToShow={4}
                    infinite={false}
                    style={{ width: "100%", objectFit: "contain" }}
                  >
                    <div className="focusedImageContainer">
                      <img
                        className="product_img"
                        src={fetchProduct?.data.data.avatarImageURL}
                        alt="avatar"
                        onClick={() => {
                          setFocusedImage(
                            fetchProduct?.data.data.avatarImageURL
                          );
                        }}
                      ></img>
                    </div>

                    {fetchProduct?.data.data.otherImagesURLS.map(
                      (image, index) => (
                        <div key={index} className="focusedImageContainer">
                          <img
                            className="product_img"
                            src={image}
                            alt=""
                            onClick={() => {
                              setFocusedImage(image);
                            }}
                          ></img>
                        </div>
                      )
                    )}
                  </Carousel>
                </Col>

                <Col xs={24} sm={24} md={16} lg={16} xl={16} xxl={16}>
                  {/*Name*/}
                  <h1 style={{ "font-weight": "bold", margin: 0 }}>
                    {fetchProduct?.data.data.name}
                  </h1>

                  {/*Rating and sales*/}
                  <div style={{ margin: "1rem" }}>
                    <a href="#ratings">
                      <Rate
                        disabled
                        defaultValue={
                          fetchProduct?.data.data.averageRatingScore
                        }
                        allowHalf={true}
                      />
                      - see {fetchProduct?.data.data.receivedRatings.length}{" "}
                      ratings
                    </a>
                    <Divider type="vertical"></Divider>
                    Sold {fetchProduct?.data.data.totalSales} products
                  </div>

                  {/*Price*/}
                  <div className="price">
                    {Formatter.addNumberSeparator(
                      (
                        fetchProduct?.data?.data?.defaultUnitPrice +
                        selectedOptions
                          .map((option) => option.priceDifference)
                          .reduce(
                            (previousValue, currentValue) =>
                              previousValue + currentValue,
                            0
                          )
                      ).toString()
                    )}{" "}
                    ₫
                  </div>

                  {/*Options*/}
                  <div style={{ margin: "1rem" }}>
                    {fetchProduct?.data?.data?.characteristics.map(
                      (char, index) => (
                        <>
                          <div key={index} style={{ fontWeight: "bold" }}>
                            {char}
                          </div>
                          <Carousel
                            key={index}
                            draggable={true}
                            slidesToScroll={4}
                            slidesToShow={4}
                            infinite={false}
                          >
                            {fetchProduct?.data?.data?.productOptions
                              .filter(
                                (option) =>
                                  option.characteristic === char &&
                                  option.status === "GENERATED"
                              )
                              .map((option, index) => (
                                <div
                                  key={option.id}
                                  className={
                                    selectedOptions.includes(option)
                                      ? "productOption-Chosen"
                                      : "productOption-notChosen"
                                  }
                                  title={option.name}
                                  style={optionBorder(option)}
                                  onClick={() => {
                                    const newOptions = selectedOptions.filter(
                                      (o) =>
                                        o.characteristic !==
                                        option.characteristic
                                    );
                                    setSelectedOptions([...newOptions, option]);
                                    setFocusedImage(option.imageUrl);
                                  }}
                                >
                                  <div className="product_option_img_container">
                                    <img
                                      className="product_img"
                                      src={option.imageUrl}
                                      alt={option.name}
                                    />
                                  </div>

                                  <div
                                    className="card-body"
                                    title={option.name}
                                  >
                                    <h2 className="product_name">
                                      {option.name}
                                    </h2>
                                  </div>
                                </div>
                              ))}
                          </Carousel>
                        </>
                      )
                    )}
                  </div>

                  {/*Seller*/}
                  <p style={{ "font-weight": "bold", margin: "1rem" }}>
                    Seller:{" "}
                    <a href={`/seller/${fetchProduct?.data.data.seller.id}`}>
                      {fetchProduct?.data.data.seller.username}
                    </a>
                  </p>

                  {/*Quantiry input*/}
                  <div style={{ margin: "1rem" }}>
                    <Form initialValues={{ quantity: 1 }} onFinish={addCart}>
                      <Space align="baseline">
                        {/* <Button
                  type="primary"
                  disabled={cartQuantity < 2}
                  onClick={() => {
                    input.current.onStep(input.current.value, {offset: 1, type:'down'});
                  }}
                >
                  <MinusOutlined />
                </Button> */}

                        <Form.Item
                          name={"quantity"}
                          label={"Order quantity:"}
                          rules={[
                            {
                              required: true,
                              message: "Please provide order quantity",
                            },

                            ({ getFieldValue }) => ({
                              validator(_, value) {
                                if (value > stock) {
                                  Modal.error({
                                    title: `There remains only ${stock} items for this product`,
                                  });
                                  return Promise.reject(
                                    new Error(
                                      `There remains only ${stock} items for this product`
                                    )
                                  );
                                }

                                return Promise.resolve();
                              },
                            }),
                          ]}
                        >
                          <InputNumber
                            ref={input}
                            min={1}
                            /*  onChange={(value) => {
                      if (value > stock) {
                        Modal.error({
                          title: `There remains only ${stock} items for this product`,
                        });
                        input.current.value = 1;
                        value = 1;
                      }
                      console.log(`HERE: VALUE: ${value}`);
                    }} */
                          ></InputNumber>
                        </Form.Item>

                        {/* <Button
                  type="primary"
                  disabled={cartQuantity > stock}
                  onClick={() => {
                    input.current.onStep(input.current.value, {
                      offset: 1,
                      type: "up",
                    });
                  }}
                >
                  <PlusOutlined />
                </Button> */}
                      </Space>
                      <Form.Item>
                        <Button type="primary" danger={true} htmlType="submit">
                          Add to cart
                        </Button>
                      </Form.Item>
                    </Form>
                  </div>
                </Col>
              </Row>
              <div style={{ marginTop: "3rem" }}>
                <Descriptions
                  title="Specific details"
                  bordered
                  column={1}
                  size="small"
                >
                  {Object.entries(fetchProduct.data?.data?.specificDetails).map(
                    (detail) => (
                      <Descriptions.Item
                        label={
                          <span style={{ fontWeight: "bold" }}>
                            {detail[0]}
                          </span>
                        }
                      >
                        {detail[1]}
                      </Descriptions.Item>
                    )
                  )}
                </Descriptions>
              </div>
              <div style={{ marginTop: "3rem" }}>
                <h1 style={{ "font-weight": "bold" }}> Product description </h1>
                {fetchProduct.data?.data?.productDescription}
              </div>
              <div style={{ marginTop: "3rem" }}>
                <h1 style={{ "font-weight": "bold" }}> Ratings </h1>
                {!Array.isArray(fetchProduct.data?.data?.receivedRatings) ||
                fetchProduct.data?.data?.receivedRatings.length < 1 ? (
                  <Empty
                    description={
                      <span style={{ fontWeight: "bold" }}>
                        There is no rating for this product for now
                      </span>
                    }
                  ></Empty>
                ) : (
                  <div>
                    <List>
                      <List.Item key={"pannel"}>
                        <List.Item.Meta
                          avatar={
                            <span
                              style={{ fontWeight: "bolder", fontSize: "2rem" }}
                            >
                              {fetchProduct.data?.data?.averageRatingScore}
                            </span>
                          }
                          title={
                            <Rate
                              value={
                                fetchProduct.data?.data?.averageRatingScore
                              }
                            ></Rate>
                          }
                          description={
                            <span>
                              {fetchProduct.data?.data?.receivedRatings.length}{" "}
                              ratings
                            </span>
                          }
                        ></List.Item.Meta>
                      </List.Item>
                    </List>
                    <List
                      itemLayout="vertical"
                      pagination={{ pageSize: 5 }}
                      dataSource={fetchProduct.data?.data?.receivedRatings}
                      renderItem={(item) => (
                        <List.Item
                          key={item.id}
                          actions={[<Button>Like</Button>]}
                        >
                          <List.Item.Meta
                            avatar={<UserOutlined></UserOutlined>}
                            title={item.creator.username}
                            description={
                              <div>
                                <Rate value={item.score}></Rate>
                              </div>
                            }
                          >
                            {item.comment.contents}
                          </List.Item.Meta>
                        </List.Item>
                      )}
                    ></List>
                  </div>
                )}
              </div>
            </div>
          </Skeleton>
        );
      }   
}


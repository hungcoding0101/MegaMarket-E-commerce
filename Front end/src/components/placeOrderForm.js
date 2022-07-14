import React from 'react'
import { Form, Input, Modal, Space, List, Image, Radio } from 'antd';
import {Formatter} from '../Util/Formatter'
import {useResetFormOnCloseModal} from './productOptionsForm'
import {productUtil} from '../Util/ProductUtil'
import { useCurrentUser } from '../Hooks/UserHooks';
import { useAllProducts } from '../Hooks/ProductHooks';


 const PlaceOrderForm = ({ visible, onCancel, orders }) => {
   const [form] = Form.useForm();

   useResetFormOnCloseModal({ form, visible });

  const user = useCurrentUser(null, null, true);

    const productList = useAllProducts(
      null,
      (error) => {
        console.log(`ERROR: ${JSON.stringify(error.response.data)}`);
      },
      true
    )?.data?.data;

   const onOk = async () => {
     console.log('SUBMITED')
     form.submit();
   };


   return (
     <Modal
       title="Orders"
       visible={visible}
       onOk={onOk}
       onCancel={onCancel}
       centered={true}
       width={"100%"}
     >
       <Form
         form={form}
         layout="vertical"
         name="placeOrderForm"
         initialValues={{ orders: orders, paymentMethods: new Map() }}
       >
         <Form.Item>
           <List
             dataSource={orders}
             renderItem={(order) => {
               /* const cartItem = user.data.data.cart.filter(
                 (item) => item.id === order.cartId
               )[0]; */
               const thisProduct = productUtil.findProduct(
                 order.product,
                 productList
               );

               console.log(`ITEM: ${JSON.stringify(order)}`);
               return (
                 <List.Item>
                   <List.Item.Meta
                     avatar={
                       <div
                         className="focusedImageContainer"
                         style={{ width: 100 }}
                       >
                         <Image
                           Item={order}
                           className="product_img"
                           src={thisProduct.avatarImageURL}
                         ></Image>
                       </div>
                     }
                     title={
                       <p>
                         <span
                           style={{
                             color: "darkslateblue",
                             fontWeight: "bold",
                           }}
                         >
                           {thisProduct.name}
                         </span>
                         {Array.isArray(order.choices) &&
                         order.choices.length > 0
                           ? order.choices.map((choice) => (
                               <span
                                 key={choice.id}
                                 style={{ fontWeight: "bold" }}
                               >
                                 {` - ${choice.characteristic}: ${choice.name}`}
                               </span>
                             ))
                           : null}
                       </p>
                     }
                     description={
                       <div style={{ color: "black" }}>
                         <p>
                           Unit price:{" "}
                           <span style={{ fontWeight: "bold" }}>
                             {Formatter.addNumberSeparator(
                               order.unitPrice.toString()
                             )}
                           </span>
                         </p>
                         <p>
                           Quantity:{" "}
                           <span style={{ fontWeight: "bold" }}>
                             {Formatter.addNumberSeparator(
                               order.quantity.toString()
                             )}
                           </span>
                         </p>
                         <p>
                           Delivery fee:{" "}
                           <span style={{ fontWeight: "bold" }}>
                             {Formatter.addNumberSeparator(
                               (order.deliveryFeePerUnit * order.quantity <
                               order.maxDeliveryFee
                                 ? order.deliveryFeePerUnit * order.quantity
                                 : order.maxDeliveryFee
                               ).toString()
                             )}
                           </span>
                         </p>
                         <p>
                           Total price:{" "}
                           <span style={{ fontWeight: "bold" }}>
                             {Formatter.addNumberSeparator(
                               ((order.deliveryFeePerUnit * order.quantity <
                               order.maxDeliveryFee
                                 ? order.deliveryFeePerUnit * order.quantity
                                 : order.maxDeliveryFee) +
                                 order.unitPrice * order.quantity).toString()
                             )}
                           </span>
                         </p>
                         <span style={{ fontWeight: "bold" }}>
                           Payment methods:
                         </span>
                         <Form.Item
                           name={order.id}
                           rules={[
                             {
                               required: true,
                               message: "Please choose payment method",
                             },
                           ]}
                         >
                           <Radio.Group
                             onChange={(e) => {
                               form
                                 .getFieldValue("paymentMethods")
                                 .set(order.id, e.target.value);
                             }}
                           >
                             <Space direction="vertical">
                               {thisProduct.paymentMethods.map((method) => (
                                 <Radio value={method}>{method}</Radio>
                               ))}
                             </Space>
                           </Radio.Group>
                         </Form.Item>
                       </div>
                     }
                   />
                 </List.Item>
               );
             }}
           ></List>
         </Form.Item>

         <Form.Item
           name="address"
           label={<span style={{ fontWeight: "bold" }}>Delivery address</span>}
           rules={[
             {
               required: true,
               message: "Please provide delivery address",
             },
           ]}
         >
           {user.data?.data?.deliveryAddresses.length > 0 ? (
             <Radio.Group>
               <Space direction="vertical">
                 {user.data.data.deliveryAddresses.map((add) => (
                   <Radio value={add}>{add}</Radio>
                 ))}
               </Space>
             </Radio.Group>
           ) : (
             <Space direction="vertical">
               <div style={{color: 'blue'}}>
                 You don't have any delivery addresses. Please provide one
               </div>
               <Input />
             </Space>
           )}
         </Form.Item>

         <Form.Item name={"optionImages"} hidden={true}></Form.Item>
       </Form>
     </Modal>
   );
 };

export default PlaceOrderForm;

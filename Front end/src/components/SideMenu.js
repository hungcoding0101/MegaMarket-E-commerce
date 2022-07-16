import { TreeSelect, Modal, Menu, Badge } from "antd";
import {
  HomeOutlined,
  AppstoreOutlined,
  UserOutlined,
  ShoppingOutlined,
  KeyOutlined,
  LoginOutlined,
  EnvironmentOutlined,
  NotificationOutlined,
} from "@ant-design/icons";
import { useProductCategoryTree } from "../Hooks/ProductCategoryHooks";
import { Link, useHistory } from "react-router-dom";
import { useContext } from "react";
import { appContext } from "../App";

const SideMenu = (props) => {
  const categoryTree = useProductCategoryTree(null, null, true);

  const context = useContext(appContext);

  const user = context.user;
  const logOut = context.logOut;

  const history = useHistory();

  const setSideMenuVisible = props.setSideMenuVisible;

  const setSignInVisible = props.setSignInVisible;

  const isCustomer = user?.data?.data.role === "ROLE_CUSTOMER";

  const getItem = (label, key, icon, children, type) => {
    return {
      key,
      icon,
      children,
      label,
      type,
    };
  };

  const items = [
    getItem(<Link to={"/"}>Home</Link>, "1", <HomeOutlined />),

    getItem(
      <TreeSelect
        treeData={categoryTree.data?.data}
        placeholder="Product categories"
        allowClear={true}
        dropdownMatchSelectWidth={false}
        dropdownStyle={{
          overflow: "clip",
          width: 300,
        }}
        onSelect={(value) => {
          history.push(
            `/product/list/category?keyWords=${encodeURIComponent(value)}`
          );

          setSideMenuVisible(false);
        }}
      ></TreeSelect>,
      "2",
      <AppstoreOutlined />
    ),

    user.isSuccess
      ? getItem(
          "Account management",

          "3",
          <KeyOutlined />,
          [
            isCustomer
              ? getItem(
                  <Link to={`/${isCustomer ? "customer" : "seller"}/orders`}>
                    Your orders
                  </Link>,
                  "3-order",
                  <ShoppingOutlined />
                )
              : null,

            getItem(
              <Link to={`/${isCustomer ? "customer" : "seller"}/basic_info`}>
                Basic info
              </Link>,
              "3-info",
              <UserOutlined />
            ),

            isCustomer
              ? getItem(
                  <Link to={"/customer/addresses"}>Address book</Link>,
                  "3-addresses",
                  <EnvironmentOutlined />
                )
              : null,
          ]
        )
      : getItem(
          <div onClick={() => setSignInVisible(true)}>Log in</div>,
          "3",
          <LoginOutlined />
        ),

    user.isSuccess
      ? getItem(
          <Link to={`/${isCustomer ? "customer" : "seller"}/notifications`}>
            Notifications{" "}
            <Badge
              count={
                user.data?.data?.notifications?.filter(
                  (element) => element.status === "NEW"
                ).length
              }
            ></Badge>
          </Link>,
          "4",
          <NotificationOutlined />
        )
      : null,

    user.isSuccess
      ? getItem(
          <div
            onClick={() => {
              logOut.mutate(null, {
                onSuccess: () => {
                  Modal.warning({
                    title: "Your have been logged out",
                    onOk: () => {
                      logOut.reset();
                    },
                  });
                },
              });
            }}
          >
            Log out
          </div>,
          "5",
          <LoginOutlined />
        )
      : null,
  ];

  return (
    <Menu
      mode="inline"
      items={items}
      onClick={(event) => {
        if (event.key !== "2") {
          setSideMenuVisible(false);
        }
      }}
    ></Menu>
  );
};

export default SideMenu;

import React from "react";
import { Row, Col, Menu, Layout } from "antd";
import {
  NotificationOutlined,
  ShoppingOutlined,
  UserOutlined,
} from "@ant-design/icons";
import { Link } from "react-router-dom";
import { Switch } from "react-router-dom";
import { useRouteMatch } from "react-router-dom";
import ProtectedRoute from "../components/ProtectedRoute";
import SellerOrders from "../components/SellerOrders";
import SellerProducts from "../components/SellerProducts";
import UploadingProduct from "../components/UploadingProduct";
import { Content } from "antd/lib/layout/layout";
import { usePrefetchOwnedProducts } from "../Hooks/SellerHooks";
import UserInfo from "../components/UserInfo";
import Notifications from "../components/Notifications";

export default function SellerHomeScreen(props) {
  let { path, url } = useRouteMatch();
  const fullPath = props.location.pathname;
  usePrefetchOwnedProducts();

  return (
    <Layout>
      <Row align="stretch">
        <Col xs={23} sm={23} md={23} lg={23} xl={23} xxl={23}>
          <Menu
            theme="dark"
            mode="horizontal"
            defaultSelectedKeys={[
              fullPath !== "/seller"
                ? fullPath.substring(fullPath.lastIndexOf("/") + 1)
                : "products",
            ]}
            items={[
              {
                key: "uploadProduct",
                icon: (
                  <Link to={`${path}/uploadProduct`}>
                    <UserOutlined />
                  </Link>
                ),
                label: "Upload product",
              },

              {
                key: "orders",
                icon: (
                  <Link to={`${path}/orders`}>
                    <NotificationOutlined />
                  </Link>
                ),
                label: "Manage orders",
              },

              {
                key: "products",
                icon: (
                  <Link to={`${path}/products`}>
                    <ShoppingOutlined />
                  </Link>
                ),
                label: "Manage products",
              },

              {
                key: "basic_info",
                icon: (
                  <Link to={`${path}/basic_info`}>
                    <UserOutlined />
                  </Link>
                ),
                label: "Basic info",
              },

              {
                key: "notifications",
                icon: (
                  <Link to={`${path}/notifications`}>
                    <NotificationOutlined />
                  </Link>
                ),
                label: "Notifications",
              },
            ]}
          ></Menu>

          <Layout>
            <Content>
              <Switch>
                <ProtectedRoute
                  path={`${path}/uploadProduct`}
                  component={UploadingProduct}
                />

                <ProtectedRoute
                  path={`${path}/orders`}
                  component={SellerOrders}
                />

                <ProtectedRoute
                  path={`${path}/products`}
                  component={SellerProducts}
                />

                <ProtectedRoute
                  path={`${path}/basic_info`}
                  component={UserInfo}
                ></ProtectedRoute>

                <ProtectedRoute
                  path={`${path}/notifications`}
                  component={Notifications}
                ></ProtectedRoute>
              </Switch>
            </Content>
          </Layout>
        </Col>
      </Row>
    </Layout>
  );
}

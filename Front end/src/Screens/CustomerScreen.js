import React from 'react'
import { useRouteMatch } from 'react-router-dom';
import { Switch } from 'react-router-dom';
import ProtectedRoute from '../components/ProtectedRoute';
import UserInfo from "../components/UserInfo";
import Notifications from "../components/Notifications";
import Orders from "../components/Orders";
import Addresses from "../components/Addresses";
import { Col, Layout, Menu, Row } from 'antd';
import { UserOutlined, ShoppingOutlined, EnvironmentOutlined, NotificationOutlined} from '@ant-design/icons';
import { Link } from 'react-router-dom';
import "antd/dist/antd.css";


const { Header, Content, Footer, Sider } = Layout;


export default function CustomerScreen(props) {
    let { path, url } = useRouteMatch();
    const fullPath = props.location.pathname;

  return (
    <Layout style={{backgroundColor: 'white', padding: 0}}>
      <Row align="stretch">
        <Col span={24}>
          <Menu
            theme="dark"
            style={{width: '100%'}}
            mode="horizontal"
            defaultSelectedKeys={[
              fullPath !== "/customer/"
                ? fullPath.substring(fullPath.lastIndexOf("/") + 1)
                : "basic_info",
            ]}
            items={[
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

              {
                key: "orders",
                icon: (
                  <Link to={`${path}/orders`}>
                    <ShoppingOutlined />
                  </Link>
                ),
                label: "Orders",
              },

              {
                key: "addresses",
                icon: (
                  <Link to={`${path}/addresses`}>
                    <EnvironmentOutlined />
                  </Link>
                ),
                label: "Addresses",
              },
            ]}
          ></Menu>

          <Layout>
            <Content >
              <Switch>
                <ProtectedRoute
                  path={`${path}/basic_info`}
                  component={UserInfo}
                ></ProtectedRoute>

                <ProtectedRoute
                  path={`${path}/notifications`}
                  component={Notifications}
                ></ProtectedRoute>

                <ProtectedRoute
                  path={`${path}/orders`}
                  component={Orders}
                ></ProtectedRoute>

                <ProtectedRoute
                  path={`${path}/addresses`}
                  component={Addresses}
                ></ProtectedRoute>
              </Switch>
            </Content>
          </Layout>
        </Col>
      </Row>
    </Layout>
  );
}

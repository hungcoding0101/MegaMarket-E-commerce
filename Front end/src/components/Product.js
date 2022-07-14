import React from 'react';
import { Link } from 'react-router-dom';
import { Formatter } from '../Util/Formatter';
import { Rate } from 'antd';  

export default function Product(props) {
  const { product } = props;
  return (
    <div key={product.id} className="card">
      <div className="product_img_container">
        <Link to={`/product/single/${product.id}`}>
          <img
            className="product_img"
            src={product.avatarImageURL}
            alt={product.name}
          />
        </Link>
      </div>

      <div className="card-body" title={product.name}>
        <Link to={`/product/single/${product.id}`}>
          <h2 className="product_name">{product.name}</h2>
        </Link>
        <Rate
          style={{
            margin: 0,
            width: 135,
            fontSize: "1.5rem",
            alignSelf: 'center',
            padding: 0,
            maxHeight: 8
          }}
          disabled
          defaultValue={product.averageRatingScore}
          allowHalf={true}
        />
        <div className="price">
          {Formatter.addNumberSeparator(product.defaultUnitPrice.toString())}{" "}
          vnđ
        </div>
      </div>
    </div>
  );
}
import React from "react";
import { Link } from "react-router-dom";

export default function ProductCategory(props) {
  const { category } = props;
  return (
    <div key={category.id} className="card">
      <div className="category_img_container">
        <Link
          to={`/product/list/category?keyWords=${encodeURIComponent(
            category.title
          )}`}
        >
          <img
            className="product_img"
            src={category.avatarImage}
            alt={category.name}
          />
        </Link>
      </div>

      <div className="card-body" title={category.name}>
        <Link
          to={`/product/list/category?keyWords=${encodeURIComponent(
            category.title
          )}`}
        >
          <h2 className="product_category_name">{category.title}</h2>
        </Link>
      </div>
    </div>
  );
}

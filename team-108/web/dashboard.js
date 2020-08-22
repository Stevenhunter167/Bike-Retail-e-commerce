class Dashboard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn: false,
            email: "",
            password: "",
            meta: {

            }
        };
    }

    componentDidMount() {
        jQuery.ajax({
            type: "POST",
            url: "api/staffService",
            data: {
                action: "meta"
            },
            success: (result) => {
                result = JSON.parse(result);
                this.setState(result);
            }
        });
    }

    login = (e) => {
        e.preventDefault();
        jQuery.ajax({
            type: "POST",
            url: "api/staffLogin",
            data: {
                email: this.state.email,
                password: this.state.password
            },
            success: (result) => {
                result = JSON.parse(result);
                console.log(result);
                if (result.Status === "success") {
                    this.setState({
                        isLoggedIn: true
                    })
                } else {
                    alert("email or password wrong");
                }
            }
        });
    };

    handleChange = (e) => {
        e.preventDefault();
        this.setState({
            [e.target.id]: e.target.value
        })
    };

    add_bike = (e) => {
        e.preventDefault();
        let data = {
            action: "add_bike",
                product_name: e.target.product_name.value,
                store_name: e.target.store_name.value,
                store_phone: e.target.store_phone.value,
                store_email: e.target.store_email.value,
                store_address: e.target.store_address.value,
                brand_name: e.target.brand_name.value,
                category_name: e.target.category_name.value,
                model_year: e.target.model_year.value,
                list_price: e.target.list_price.value,
                rating: e.target.rating.value,
                quantity: e.target.quantity.value
        };
        console.log(data);
        jQuery.ajax({
            type: "POST",
            url: "api/staffService",
            data,
            success: (result) => {
                result = JSON.parse(result);
                console.log(result);
                // alert(result.add_bike);
                if (typeof result.add_bike === "string") {
                    alert(result.add_bike);
                } else {
                    alert(result.add_bike[0].answer);
                }

            }
        });
    };

    add_store = (e) => {
        e.preventDefault();
        jQuery.ajax({
            type: "POST",
            url: "api/staffService",
            data: {
                action: "add_store",
                store_name: e.target.store_name.value,
                store_phone: e.target.store_phone.value,
                store_email: e.target.store_email.value,
                store_address: e.target.store_address.value
            },
            success: (result) => {
                result = JSON.parse(result);
                console.log(result);
                // alert(result.add_bike);
                if (typeof result.add_store === "string") {
                    alert(result.add_store);
                } else {
                    console.log(result.add_store[0]);
                    alert(result.add_store[0].answer);
                }

            }
        });
    };

    render() {
        return (
            <div align="center">
                <h2 align="center">Admin{(!this.state.isLoggedIn) ? " Login" : " Main Page"}</h2>
                {(!this.state.isLoggedIn) ? <div align="center"><form onSubmit={this.login}>
                    Email: <input id="email" type="text" name="email" placeholder="classta@email.edu" onChange={this.handleChange}/>
                    Password: <input id="password" type="password" name="password" onChange={this.handleChange}/>
                    <input type="submit" value="Submit"/>
                </form></div> : <div> </div>}
                {(this.state.isLoggedIn)
                    ?
                    <div>


                        <table>
                            <tr>
                                <td>
                                    <div id="meta">
                                        <h4 align="center">Meta Data</h4>
                                        <table>
                                            <th>TABLE_NAME</th>
                                            <th>COLUMN_NAME</th>
                                            <th>COLUMN_TYPE</th>
                                            <th>DATA_TYPE</th>
                                            <th>COLUMN_KEY</th>
                                            <th>IS_NULLABLE</th>
                                            <th>NUMERIC_PRECISION</th>
                                            <th>CHARACTER_MAXIMUM_LENGTH</th>
                                            <th>EXTRA</th>
                                            {
                                                Object.values(this.state.meta).map(
                                                    (item) => (
                                                        <tr>
                                                            <td>{item.TABLE_NAME}</td>
                                                            <td>{item.COLUMN_NAME}</td>
                                                            <td>{item.COLUMN_TYPE}</td>
                                                            <td>{item.DATA_TYPE}</td>
                                                            <td>{item.COLUMN_KEY}</td>
                                                            <td>{item.IS_NULLABLE}</td>
                                                            <td>{item.NUMERIC_PRECISION}</td>
                                                            <td>{item.CHARACTER_MAXIMUM_LENGTH}</td>
                                                            <td>{item.EXTRA}</td>
                                                        </tr>
                                                    )
                                                )
                                            }
                                        </table>
                                    </div>
                                </td>
                                <td>
                                    <div>
                                        <div>
                                            <h4 align="center">add bike</h4>
                                            <form id="add_bike" onSubmit={this.add_bike}>
                                                <table>
                                                    <tr>
                                                        <td>Product name*</td>
                                                        <td><input name="product_name"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Store name</td>
                                                        <td><input name="store_name"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Store phone</td>
                                                        <td><input name="store_phone"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Store email</td>
                                                        <td><input name="store_email"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Store address</td>
                                                        <td><input name="store_address"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Brand name</td>
                                                        <td><input name="brand_name"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Category name*</td>
                                                        <td>
                                                            <select id="category_name" type="text">
                                                                <option></option>
                                                                <option>Children Bicycles</option>
                                                                <option>Comfort Bicycles</option>
                                                                <option>Cruisers Bicycles</option>
                                                                <option>Cyclocross Bicycles</option>
                                                                <option>Electric Bikes</option>
                                                                <option>Mountain Bikes</option>
                                                                <option>Road Bikes</option>
                                                                <option>Time Trial Bikes</option>
                                                                <option>Trick Bikes</option>
                                                                <option>Commuting Bikes</option>
                                                                <option>Track Bikes</option>
                                                                <option>Tandems</option>
                                                                <option>Adult Trikes</option>
                                                                <option>Folding Bike</option>
                                                                <option>Recumbents</option>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>Model year*</td>
                                                        <td><input name="model_year"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Price*</td>
                                                        <td><input name="list_price"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Rating*</td>
                                                        <td><input name="rating"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Quantity*</td>
                                                        <td><input name="quantity"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td></td>
                                                        <td><input type="submit" value="Submit"/></td>
                                                    </tr>
                                                </table>
                                            </form>
                                        </div>
                                        <div>
                                            <h4 align="center">add store</h4>
                                            <form id="add_bike" onSubmit={this.add_store}>
                                                <table>
                                                    <tr>
                                                        <td>Store name</td>
                                                        <td><input name="store_name"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Store phone</td>
                                                        <td><input name="store_phone"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Store email</td>
                                                        <td><input name="store_email"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Store address</td>
                                                        <td><input name="store_address"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td></td>
                                                        <td><input type="submit" value="Submit"/></td>
                                                    </tr>
                                                </table>
                                            </form>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    : <div> </div>}
            </div>
        );
    }

}

ReactDOM.render(<Dashboard/>, document.getElementById("dashboard"));
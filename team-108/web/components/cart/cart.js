// const Bikes = ({items, modhandler}) => {
//     return (
//
//     );
// };

class Cart extends React.Component{

    constructor(props) {
        super(props);

        this.state = {
            error: null,
            isloaded: false,
            items: {},

            first_name: "",
            last_name: "",
            card_id: "",
            exp_date: "",

            paying: false,
            confirm: false,
            confirmation_info: null
        };

    }

    componentDidMount() {
        jQuery.ajax({
            type: "GET",
            data: {
                action: "lookup"
            },  // add data here
            url: "api/cart",
            success: (result) => {
                this.setState({
                    isLoaded: true,
                    items: JSON.parse(result)
                });
                console.log(JSON.parse(result));
            }
        });
    }

    handleModification = (e) => {
        e.preventDefault();
        const id = e.target.id.slice(0,e.target.id.length - 1);
        const mod = e.target.id.slice(e.target.id.length - 1, e.target.id.length);
        console.log(id, mod);

        const map = {
            ["+"]: "buy",
            ['-']: "pop",
            d: "remove"
        };

        console.log({
            action: map[mod],
                id: id
        });

        jQuery.ajax({
            type: "GET",
            data: {
                action: map[mod],
                product_id: id
            },
            url: "api/cart",
            success: (result) => {
                console.log(JSON.parse(result));
                this.setState({
                    items: JSON.parse(result)
                });
            }
        })
    };

    handleCheckout = (e) => {
        e.preventDefault();
        this.setState({
            paying: true
        })
    };

    handleInput = (e) => {
        e.preventDefault();
        this.setState({
            [e.target.id]: e.target.value
        });
    };

    handlePlaceOrder = (e) => {
        e.preventDefault();
        this.setState({
            paying: false,
            confirm: true,
            confirmation_info: null
        });



        jQuery.ajax({
            type: "POST",
            data: {
                first_name : this.state.first_name,
                last_name: this.state.last_name,
                id: this.state.card_id,
                expiration: this.state.exp_date
            },
            url: "api/payment",
            success: (result) => {
                result = JSON.parse(result);
                if (result.success === true) {
                    this.setState({
                        paying: false,
                        confirm: true,
                        confirmation_info: {
                            success: true,
                            order_id: result.confirmation.order_id
                        }
                    });
                } else {
                    this.setState({
                        paying: true,
                        confirm: false
                    });

                    alert("Invalid payment info, please review your inputs");

                }
            }
        });
    };

    render() {
        console.log(this.state.items);
        Object.keys(this.state.items).map(
            key => {
                console.log(this.state.items[key].itemInfo[0].product_name);
            }
        );

        if (this.state.error) {
            return <div>Error: {this.state.error.message}</div>;
        } else if (!this.state.isLoaded) {
            return <div className="center">Loading...</div>;
        } else if (!this.state.confirm){
            return (
                <div>
                <div className={(this.state.paying) ? "dim" : ""}>
                    <div>
                        <table className="table">
                            <col width="230"></col>
                            <col width="150"></col>
                            <col width="100"></col>
                            <col width="180"></col>
                            <col width="50"></col>
                            <col width="50"></col>
                            <col width="100"></col>

                            <th>product name</th>
                            <th>price</th>
                            <th>quantity</th>
                            <th>total price</th>
                            <th></th>
                            <th></th>
                            <th></th>
                            <tbody>
                            {Object.keys(this.state.items).map(
                                key => {
                                    return (
                                        <tr>
                                            <td>
                                                <a href={"/team-108/singleBike.html?bikeid=" + key}>
                                                    {this.state.items[key].itemInfo[0].product_name}
                                                </a>
                                            </td>
                                            <td>${this.state.items[key].itemInfo[0].list_price}</td>
                                            <td>{this.state.items[key].number}</td>
                                            <td>${this.state.items[key].itemInfo[0].list_price * this.state.items[key].number}</td>
                                            <td>
                                                <button id={key + "+"} onClick={this.handleModification}>+</button>
                                            </td>
                                            <td>
                                                <button id={key + "-"} onClick={this.handleModification}>-</button>
                                            </td>
                                            <td>
                                                <button id={key + "d"} onClick={this.handleModification}>remove</button>
                                            </td>
                                        </tr>
                                    );
                                }
                            )}
                            <tr>
                                <td>
                                    <strong>total: </strong>
                                </td>
                                <td>
                                    {
                                        (() => {
                                            let total = 0;
                                            for (let value of Object.values(this.state.items)) {
                                                let price = parseFloat(value.itemInfo[0].list_price);
                                                let count = parseFloat(value.number);
                                                console.log(price);
                                                console.log(count);
                                                total += price * count;
                                            }
                                            return total;
                                        }) ()
                                    }
                                </td>
                                <td> </td>
                                <td> </td>
                                <td> </td>
                                <td> </td>
                                <td><button className="action" id="checkout" disabled={this.state.paying || Object.keys(this.state.items).length === 0} onClick={this.handleCheckout}>checkout</button></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                    {
                        (this.state.paying) ?
                            <div className="center">
                                <strong> Please enter payment information below</strong>
                                <table>
                                    <col width="230"/>
                                    <col width="130"/>
                                    <tr>
                                        <td>cardholder first name: </td>
                                        <td><input id="first_name" onChange={this.handleInput} placeholder="ex: John"/></td>
                                    </tr>
                                    <tr>
                                        <td>cardholder last name: </td>
                                        <td><input id="last_name" onChange={this.handleInput} placeholder="ex: Doe"/></td>
                                    </tr>
                                    <tr>
                                        <td>card number: </td>
                                        <td><input id="card_id" onChange={this.handleInput} placeholder="ex: 1234567890"/></td>
                                    </tr>
                                    <tr>
                                        <td>expiration date: </td>
                                        <td><input id="exp_date" onChange={this.handleInput} placeholder="ex: 2023/05/10"/></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div align="left">
                                                <button onClick={() => {this.setState({paying: false})}}>back</button>
                                            </div>

                                        </td>
                                        <td>
                                            <div align="right">
                                                <button className="action" onClick={this.handlePlaceOrder}>confirm</button>
                                            </div>
                                        </td>
                                    </tr>
                                </table>

                            </div>
                            :
                            <div> </div>
                    }
                </div>

            );
        } else if (this.state.confirm) {
            if (this.state.confirmation_info === null) {
                return (
                    <div>
                        <h3>Processing Request, please wait...</h3>
                    </div>
                );
            } else if (this.state.confirmation_info.success === false) {
                    return <div>payment information wrong</div>
            } else {
                return (
                    <div>
                        <h4> Order id: {this.state.confirmation_info.order_id}, Thank you for your purchase! </h4>
                        <div>
                            <br/>
                            <div>
                                <table className="table">
                                    <col width="230"></col>
                                    <col width="150"></col>
                                    <col width="100"></col>
                                    <col width="180"></col>
                                    <col width="50"></col>
                                    <col width="50"></col>
                                    <col width="100"></col>

                                    <th>product name</th>
                                    <th>price</th>
                                    <th>quantity</th>
                                    <th>total price</th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <tbody>
                                    {Object.keys(this.state.items).map(
                                        key => {
                                            return (
                                                <tr>
                                                    <td>
                                                        <a href={"/team-108/singleBike.html?bikeid=" + key}>
                                                            {this.state.items[key].itemInfo[0].product_name}
                                                        </a>
                                                    </td>
                                                    <td>${this.state.items[key].itemInfo[0].list_price}</td>
                                                    <td>{this.state.items[key].number}</td>
                                                    <td>${this.state.items[key].itemInfo[0].list_price * this.state.items[key].number}</td>
                                                    <td>

                                                    </td>
                                                    <td>

                                                    </td>
                                                    <td>

                                                    </td>
                                                </tr>
                                            );
                                        }
                                    )}
                                    <tr>
                                        <td>
                                            <strong>total: </strong>
                                        </td>
                                        <td>$
                                            {
                                                // calculate total
                                                (() => {
                                                    let total = 0;
                                                    for (let value of Object.values(this.state.items)) {
                                                        let price = parseFloat(value.itemInfo[0].list_price);
                                                        let count = parseFloat(value.number);
                                                        console.log(price);
                                                        console.log(count);
                                                        total += price * count;
                                                    }
                                                    return total;
                                                }) ()
                                            }
                                        </td>
                                        <td> </td>
                                        <td> </td>
                                        <td> </td>
                                        <td> </td>
                                        <td> Thank you! </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                );
            }
        }
    }

}

ReactDOM.render(<Cart/>, document.getElementById("cart"));
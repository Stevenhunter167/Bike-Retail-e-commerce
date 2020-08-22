class SingleStoreBikes extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            storeid: getUrlVars()["storeid"],
            isLoaded: false,
            page: 1,
            bikes : []
        };
    }

    componentDidMount() {
        jQuery.ajax({
            type: "GET",
            url: "api/singlestorebikes",
            data: {
                storeid: this.state.storeid,
                limit: 25,
                offset: 25 * (this.state.page - 1)
            },
            success: (result) => {
                console.log({
                    bikeid: this.state.storeid,
                    limit: 25,
                    offset: 25 * (this.state.page - 1)
                });
                this.setState({
                    isLoaded: true,
                    bikes: JSON.parse(result)
                });
            }
        });
    }

    updateList = () => {
        jQuery.ajax({
            type: "GET",
            url: "api/singlestorebikes",
            data: {
                storeid: this.state.storeid,
                limit: 25,
                offset: 25 * (this.state.page - 1)
            },
            success: (result) => {
                console.log({
                    bikeid: this.state.storeid,
                    limit: 25,
                    offset: 25 * (this.state.page - 1)
                });
                console.log(result);
                this.setState({
                    isLoaded: true,
                    bikes: JSON.parse(result)
                });
            }
        });
    };

    next_page = (e) => {
        e.preventDefault();
        this.setState({
            page: this.state.page + 1
        }, this.updateList);
    };

    prev_page = (e) => {
        e.preventDefault();
        if (this.state.page === 1) {
            return;
        } else {
            this.setState({
                page: this.state.page - 1
            }, this.updateList);
        }
    };

    render() {
        if (this.state.error) {
            return <div>Error: {this.state.error.message}</div>;
        } else if (!this.state.isLoaded) {
            return <div>Loading...</div>;
        }

        let addToCart = (e) => {
            const id = e.target.id;
            jQuery.ajax({
                type: "GET",
                data: {
                    action: "buy",
                    product_id: id
                },
                url: "api/cart",
                success: (result) => {
                    result = JSON.parse(result);
                    console.log(result);
                    alert("You now have: " + result[id].itemInfo[0].product_name + " x" + result[id].number);
                }
            })
        };

        return (
            <div>
                <strong><br/>bikes currently in-stock of this store:<br/><br/></strong>
                <button onClick={this.prev_page}>prev</button> Page: {this.state.page}<button onClick={this.next_page}>next</button>
                <table className="table">
                    <col width="230"></col>
                    <col width="150"></col>
                    <col width="200"></col>
                    <col width="100"></col>
                    <col width="180"></col>
                    <col width="100"></col>
                    <col width="100"></col>
                    <th>product name</th><th>add to cart</th><th>brand name</th><th>year</th><th>category name</th><th>price</th><th>rating</th>
                    <tbody>
                    {
                        Object.values(this.state.bikes).map(
                            item => (
                                <tr>
                                    <td>
                                        <a href={"/team-108/singleBike.html?bikeid=" + item.product_id}>
                                            {item.product_name}
                                        </a>
                                    </td>
                                    <td><img id={item.product_id} width="30" src="/team-108/cart.png" onClick={addToCart}/></td>
                                    <td>{item.brand_name}</td>
                                    <td>{item.model_year}</td>
                                    <td>{item.category_name}</td>
                                    <td>{item.list_price}</td>
                                    <td>{item.rating}</td>
                                </tr>
                            )
                        )
                    }
                    </tbody>
                </table>
            </div>
        );
    }

}

ReactDOM.render(<SingleStoreBikes/>, document.getElementById("singleStoreBikes"));
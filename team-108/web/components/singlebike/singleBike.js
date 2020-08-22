class SingleBike extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            bikeid: getUrlVars()['bikeid'],
            isLoaded: false,
            bikeinfo: {

            }
        };
    }

    componentDidMount() {
        jQuery.ajax({
            type: "GET",
            url: "api/singlebike",
            data: {
                bikeid: this.state.bikeid
            },
            success: (result) => {
                this.setState({
                    isLoaded: true,
                    bikeinfo: JSON.parse(result)[0]
                })
            }
        });
    }

    render() {
        if (this.state.error) {
            return <div>Error: {this.state.error.message}</div>;
        } else if (!this.state.isLoaded) {
            return <div>Loading...</div>;
        }

        let addToCart = (e) => {
            const id = this.state.bikeid;
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
                <table class="table">
                    <col width="200"></col>
                    <col width="200"></col>
                    <th><strong><br/>bike info:</strong></th>
                    <th></th>
                    <tbody>
                    {
                        Object.keys(this.state.bikeinfo).map(
                            key => (
                                <tr>
                                    <td>{key}</td>
                                    <td>{this.state.bikeinfo[key]}</td>
                                </tr>
                            )
                        )
                    }

                    <tr>
                        <td><strong>add to cart</strong></td>
                        <td><img id={this.state.bikeid} width="30" src="/team-108/cart.png" onClick={addToCart}/></td>
                    </tr>

                    </tbody>
                </table>


            </div>
        );
    }

}

ReactDOM.render(<SingleBike/>, document.getElementById("singleBike"));
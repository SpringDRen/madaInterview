var LikeButton = React.createClass({
  getInitialState: function() {
    return {liked: false,count: 0};
  },
  handleClick: function(event) {
    this.setState({liked: !this.state.liked,count:this.state.count+1});
  },
  render: function() {
    var text = this.state.liked ? 'like' : 'haven\'t liked';
    var clickount = this.state.count
    return (
      <p onClick={this.handleClick}>
        You {text} this. Click to toggle.
        ClickCount: {clickount}
      </p>
    );
  }
});

ReactDOM.render(
  <LikeButton />,
  document.getElementById('example')
);
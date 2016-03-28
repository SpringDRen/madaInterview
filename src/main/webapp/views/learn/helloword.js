//helloword demo1
ReactDOM.render(
  <h1>Hello, world!</h1>,
  document.getElementById('hellorworld')
);

//comment demo1
var CommentBox = React.createClass({
  render: function() {
    return (
      <div className="commentBox">
        Hello, world! I am a CommentBox.
      </div>
    );
  }
});

ReactDOM.render(
  <CommentBox />,
  document.getElementById('content')
);

//demo 2
var CommentBox2 = React.createClass({
  displayName: 'CommentBox23',//???? alert(CommentBox2.displayName)
  render: function() {
    return (
      React.createElement(
        'div',
        {className: "commentBox2"},
        "Hello, world! I am a CommentBox222."
      )
    );
  }//end render
});
ReactDOM.render(
  React.createElement(CommentBox2, null), // == <CommentBox2 />,
  document.getElementById('content2')
);

//helloword demo2
var HelloWorld = React.createClass({
  render: function() {
    return (
      <p>
        Hello, <input type="text" placeholder="Your name here" />!
        It is {this.props.date.toTimeString().replace(/^(.+ )(\(([^\)]+)\))?$/, "$1")}
      </p>
    );
  }//end render
});

setInterval(function() {
  ReactDOM.render(
    <HelloWorld date={new Date()} />,
    document.getElementById('hellorworld2')
  );
}, 500);

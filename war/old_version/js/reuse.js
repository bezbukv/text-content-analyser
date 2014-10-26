// http://net.tutsplus.com/tutorials/javascript-ajax/fully-understanding-the-this-keyword/
// http://stackoverflow.com/questions/2025789/preserving-a-reference-to-this-in-javascript-prototype-functions
// http://underscorejs.ru/#
function Tuner(selector, countItems, objUp, objDown) {
  this.seed_ = 0;
  this.selector_ = selector;
  this.totalItems_ = countItems;
  this.objUp_ = objUp;
  this.objDown_ = objDown;
}

Tuner.prototype.tick = function (obj) {
  var self = this;
  $(obj).parent().find(this.selector_).each(function (key, value) {
    // this is corrupted
    $(this).css("z-index", (self.seed_+key)%self.totalItems_);
  });
}

Tuner.prototype.up = function() {
  // this is corrupted
  this.seed_ = (this.seed_+1)%this.totalItems_;
  console.log(this.seed_);
  this.tick(this.objUp_);
}
        
Tuner.prototype.down = function() {
  // this is corrupted
  this.seed_ = (this.seed_-1+this.totalItems_)%this.totalItems_;
  this.tick(this.objDown_);
}
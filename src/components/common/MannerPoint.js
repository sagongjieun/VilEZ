const MannerPoint = (point) => {
  /** 단계명은 임시 */
  if (point <= 10) return "Lv.1";
  else if (point > 10 && point <= 20) return "Lv.2";
  else if (point > 20 && point <= 30) return "Lv.3";
  else if (point > 30 && point <= 40) return "Lv.4";
  else return "Lv.5";
};

export default MannerPoint;

const MannerPoint = (point) => {
  /** 단계명은 임시 */
  if (point <= 10) return "1단계";
  else if (point > 10 && point <= 20) return "2단계";
  else if (point > 20 && point <= 30) return "3단계";
  else if (point > 30 && point <= 40) return "4단계";
  else return "5단계";
};

export default MannerPoint;

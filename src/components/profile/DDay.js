function DDay(specificDay) {
  const today = new Date();
  const dday = new Date(specificDay);
  const gap = dday.getTime() - today.getTime();
  const result = Math.ceil(gap / (1000 * 60 * 60 * 24));
  // console.log(today, dday, gap, result);
  // console.log(specificDay);
  return result;
}
export default DDay;

package br.com.don.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Util {


	public static LocalDate converteLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}


	public static String localDateFormatado(LocalDate localDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return formatter.format(localDate);
	}


	public static LocalDate converteDataHoraLocalDate(String data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		return LocalDate.parse(data,formatter);
	}


	public static LocalDate converteDataLocalDate(String data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(data,formatter);
	}


	public static String dataSemanaFormatada(LocalDate data) {
		return localDateFormatado(data).concat(System.lineSeparator()).concat(diaDaSemana(data.getDayOfWeek()));
	}


	public static String diaDaSemana(DayOfWeek dia) {
		String retorno = dia.toString();
		switch (dia) {
		case SUNDAY:
			retorno = "Domingo";
			break;
		case MONDAY:
			retorno = "Segunda-Feira";
			break;
		case TUESDAY:
			retorno = "Terça-Feira";
			break;
		case WEDNESDAY:
			retorno = "Quarta-Feira";
			break;
		case THURSDAY:
			retorno = "Quinta-Feira";
			break;
		case FRIDAY:
			retorno = "Sexta-Feira";
			break;
		case SATURDAY :
			retorno = "Sábado";
			break;
		}
		return retorno;
	}
}
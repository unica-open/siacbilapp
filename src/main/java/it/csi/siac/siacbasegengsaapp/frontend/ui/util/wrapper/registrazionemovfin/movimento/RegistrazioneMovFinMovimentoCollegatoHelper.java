/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.movimento;

import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.Rateo;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.Risconto;

/**
 * Factory per Elemento delle scritture per lo step 1 della PrimaNotaIntegrata
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/04/2015
 *
 */
public final class RegistrazioneMovFinMovimentoCollegatoHelper {
	/** Non instanziare la classe */
	private RegistrazioneMovFinMovimentoCollegatoHelper() {
	}
	
	/**
	 * @return the registrazioneMovFin
	 */
	private static final RegistrazioneMovFin getRegistrazioneMovFin(PrimaNota primaNota) {
		return primaNota != null 
			&& primaNota.getListaMovimentiEP() != null 
			&& !primaNota.getListaMovimentiEP().isEmpty()
			&& primaNota.getListaMovimentiEP().get(0) != null
				? primaNota.getListaMovimentiEP().get(0).getRegistrazioneMovFin()
				: null;
	}
	
	/**
	 * Gets the numero movimento.
	 *
	 * @param primaNota the prima nota
	 * @return the numeroMovimento
	 */
	public static final String getNumeroMovimento(PrimaNota primaNota) {
		if(primaNota == null) {
			return "Prima nota non presente";
		}
		RegistrazioneMovFin registrazioneMovimentoFin = getRegistrazioneMovFin(primaNota);
		
		return getNumeroMovimentoFromRegistrazione(registrazioneMovimentoFin, primaNota);
	}
	
	/**
	 * Gets the numero movimento from registrazione.
	 *
	 * @param registrazioneMovimentoFin the registrazione movimento fin
	 * @return the numero movimento from registrazione
	 */
	public static String getNumeroMovimentoFromRegistrazione(RegistrazioneMovFin registrazioneMovimentoFin) {
		return getNumeroMovimentoFromRegistrazione(registrazioneMovimentoFin, null);
	}
	

	/**
	 * Ottiene il numero del movimento a partire dalla registrazione
	 * @param registrazioneMovimentoFin la registrazione
	 * @param primaNota la prima nota
	 * @return il numero del movimento
	 */
	public static String getNumeroMovimentoFromRegistrazione(RegistrazioneMovFin registrazioneMovimentoFin, PrimaNota primaNota) {
		if(registrazioneMovimentoFin == null) {
			// Se non ho la registrazione, esco
			return "";
		}
		
		Entita entita = registrazioneMovimentoFin.getMovimento();
		
		if(entita instanceof ModificaMovimentoGestione){
			return calcolaNumeroMovimentoPerEntita((ModificaMovimentoGestione)entita);
		}
		if(entita instanceof MovimentoGestione) {
			return calcolaNumeroMovimentoPerEntita((MovimentoGestione)entita);
		}
		if(entita instanceof Documento) {
			return calcolaNumeroMovimentoPerEntita((Documento<?, ?>)entita);
		}
		if(entita instanceof Subdocumento) {
			Subdocumento<?, ?> subdocumento = (Subdocumento<?, ?>)entita;
			return calcolaNumeroMovimentoPerEntita(subdocumento.getDocumento());
		}
		if(entita instanceof Liquidazione) {
			return calcolaNumeroMovimentoPerEntita((Liquidazione)entita);
		}
		if(entita instanceof Ordinativo) {
			return calcolaNumeroMovimentoPerEntita((Ordinativo)entita);
		}
		if(entita instanceof RichiestaEconomale) {
			return calcolaNumeroMovimentoPerEntita((RichiestaEconomale)entita);
		}
		if(entita instanceof RendicontoRichiesta) {
			return calcolaNumeroMovimentoPerEntita((RendicontoRichiesta)entita);
		}
		if(entita instanceof Rateo){
			return calcolaNumeroMovimentoPerEntita((Rateo)entita, primaNota);
		}
		if(entita instanceof Risconto){
			return calcolaNumeroMovimentoPerEntita((Risconto)entita, primaNota);
		}
		
		return "";
	}
	
	/**
	 * Calcolo del numero di movimento per ModificaMovimentoGestione.
	 * <br/>
	 * Anno + numero + submovimento (se presente) per i movimenti.
	 * 
	 * @param modificaMovimentoGestione la modifica del movimento di gestione per cui calcolare il numero di movimento
	 * @return il numero
	 */
	private static final  String calcolaNumeroMovimentoPerEntita(ModificaMovimentoGestione modificaMovimentoGestione) {
		StringBuilder sb = new StringBuilder();
		
		int anno = 0;
		BigDecimal numero = null;
		BigDecimal numeroSub = null;
		String terminal = "";
		
		if(modificaMovimentoGestione instanceof ModificaMovimentoGestioneSpesa){
			ModificaMovimentoGestioneSpesa mod = (ModificaMovimentoGestioneSpesa)modificaMovimentoGestione;
			terminal = " (imp)";
			Impegno impegno = mod.getImpegno();
			SubImpegno subimpegno = mod.getSubImpegno();
			if(impegno != null){
				numero = impegno.getNumero();
				anno = impegno.getAnnoMovimento();
			}else if(subimpegno != null){
				numero = subimpegno.getNumeroImpegnoPadre();
				numeroSub = subimpegno.getNumero();
				anno = subimpegno.getAnnoImpegnoPadre();
			}
		}
		
		if(modificaMovimentoGestione instanceof ModificaMovimentoGestioneEntrata){
			ModificaMovimentoGestioneEntrata mod = (ModificaMovimentoGestioneEntrata)modificaMovimentoGestione;
			terminal = " (acc)";
			Accertamento accertamento = mod.getAccertamento();
			SubAccertamento subaccertamento = mod.getSubAccertamento();
			if(accertamento != null){
				numero = accertamento.getNumero();
				anno = accertamento.getAnnoMovimento();
			}else if(subaccertamento != null){
				numero = subaccertamento.getNumeroAccertamentoPadre();
				numeroSub = subaccertamento.getNumero();
				anno = subaccertamento.getAnnoAccertamentoPadre();
			}
		}
		
		sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(modificaMovimentoGestione.getDescrizioneModificaMovimentoGestione()))
			.append("\" data-html=\"true\">")
			.append(anno);
		
		// Numero
		if(numero != null) {
			sb.append(" / ")
				.append(numero.toPlainString());
		}
		if(numeroSub != null) {
			sb.append(" - ")
				.append(numeroSub.toPlainString());
		}
		
		sb.append(terminal)
			.append("</a>");
		return sb.toString();
	}
	
	/**
	 * Calcolo del numero di movimento per il movimento di gestione.
	 * <br/>
	 * Anno + numero + submovimento (se presente) per i movimenti.
	 * 
	 * @param movimentoGestione il movimento di gestione per cui calcolare il numero di movimento
	 * @return il numero
	 */
	private static final  String calcolaNumeroMovimentoPerEntita(MovimentoGestione movimentoGestione) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
			.append(movimentoGestione.getDescrizione() != null ? FormatUtils.formatHtmlAttributeString(movimentoGestione.getDescrizione()) : "")
			.append("\" data-html=\"true\">")
			.append(movimentoGestione.getAnnoMovimento());
		
		BigDecimal numero = movimentoGestione.getNumero();
		BigDecimal numeroSub = null;
		String terminal = "";
		
		if(movimentoGestione instanceof Impegno) {
			terminal = " (imp)";
			
			if(movimentoGestione instanceof SubImpegno) {
				numero = ((SubImpegno) movimentoGestione).getNumeroImpegnoPadre();
				numeroSub = movimentoGestione.getNumero();
			}
		} else if(movimentoGestione instanceof Accertamento) {
			terminal = " (acc)";
			
			if(movimentoGestione instanceof SubAccertamento) {
				numero = ((SubAccertamento) movimentoGestione).getNumeroAccertamentoPadre();
				numeroSub = movimentoGestione.getNumero();
			}
		}
		
		// Numero
		if(numero != null) {
			sb.append(" / ")
				.append(numero.toPlainString());
		}
		if(numeroSub != null) {
			sb.append(" - ")
				.append(numeroSub.toPlainString());
		}
		
		sb.append(terminal)
			.append("</a>");
		return sb.toString();
	}
	
	/**
	 * Calcolo del numero di movimento per il documento.
	 * <br/>
	 * Anno + numero documento + tipo documento + codice soggetto per i documenti.
	 * 
	 * @param documento il documento per cui calcolare il numero di movimento
	 * @return il numero
	 */
	private static final  String calcolaNumeroMovimentoPerEntita(Documento<?, ?> documento) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(documento.getDescrizione()))
			.append("\" data-html=\"true\">")
			.append(documento.getAnno())
			.append(" / ")
			.append(documento.getNumero());
		if(documento.getTipoDocumento() != null) {
			sb.append(" / ")
				.append(documento.getTipoDocumento().getCodice());
		}
		if(documento.getSoggetto() != null) {
			sb.append(" / ")
				.append(documento.getSoggetto().getCodiceSoggetto())
				.append(" ")
				.append(documento.getSoggetto().getDenominazione());
		}
		
		sb.append("</a>");
		return sb.toString();
	}
	
	/**
	 * Calcolo del numero di movimento per la liquidazione.
	 * <br/>
	 * Anno + numero per le liquidazioni e gli ordinativi.
	 * 
	 * @param liquidazione la liquidazione per cui calcolare il numero di movimento
	 * @return il numero
	 */
	private static final  String calcolaNumeroMovimentoPerEntita(Liquidazione liquidazione) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(liquidazione.getDescrizioneLiquidazione()))
			.append("\" data-html=\"true\">")
			.append(liquidazione.getAnnoLiquidazione());
		
		if(liquidazione.getNumeroLiquidazione() != null) {
			sb.append(" / ")
				.append(liquidazione.getNumeroLiquidazione().toPlainString());
		}
		
		return sb.toString();
	}
	
	/**
	 * Calcolo del numero di movimento per l'ordinativo.
	 * <br/>
	 * Anno + numero per le liquidazioni e gli ordinativi.
	 * 
	 * @param ordinativo l'ordinativo per cui calcolare il numero di movimento
	 * @return il numero
	 */
	private static final  String calcolaNumeroMovimentoPerEntita(Ordinativo ordinativo) {
		StringBuilder sb = new StringBuilder();
		//SIAC-6002
		String strMsg = "";
		String strAsterisco = "";
		if(ordinativo.getDataSpostamento() != null){
			strMsg = "<br /><b>Spostata in data </b>" + FormatUtils.formatDate(ordinativo.getDataSpostamento()) ;
			strAsterisco = "*";
		}
		
		sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(ordinativo.getDescrizione()))
			.append(strMsg)
			.append("\" data-html=\"true\">")
			.append(ordinativo.getAnno())
			.append(" / ")
			.append(ordinativo.getNumero())
			.append(strAsterisco)
			.append("</a>");
		return sb.toString();
	}
	
	/**
	 * Calcolo del numero di movimento per la richiesta economale.
	 * <br/>
	 * Anno+numero del movimento della richiesta se cassa economale.
	 * 
	 * @param richiestaEconomale la richiesta economale per cui calcolare il numero di movimento
	 * @return il numero
	 */
	private static final  String calcolaNumeroMovimentoPerEntita(RichiestaEconomale richiestaEconomale) {
		if(richiestaEconomale.getImpegno() == null) {
			return "";
		}
		return new StringBuilder()
			.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(richiestaEconomale.getDescrizioneDellaRichiesta()))
			.append("\" data-html=\"true\">")
			.append(richiestaEconomale.getImpegno().getAnnoMovimento())
			.append(" / ")
			.append(richiestaEconomale.getImpegno().getNumero() != null ? richiestaEconomale.getImpegno().getNumero().toPlainString() : "")
			.toString();
	}
	
	/**
	 * Calcolo del numero di movimento per la richiesta economale.
	 * <br/>
	 * Anno+numero del movimento della richiesta se cassa economale.
	 * 
	 * @param rendicontoRichiesta la richiesta economale per cui calcolare il numero di movimento
	 * @return il numero
	 */
	private static final  String calcolaNumeroMovimentoPerEntita(RendicontoRichiesta rendicontoRichiesta) {
		if(rendicontoRichiesta.getImpegno() == null || rendicontoRichiesta.getRichiestaEconomale() == null) {
			return "";
		}
		return new StringBuilder()
			.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(rendicontoRichiesta.getRichiestaEconomale().getDescrizioneDellaRichiesta()))
			.append("\" data-html=\"true\">")
			.append(rendicontoRichiesta.getImpegno().getAnnoMovimento())
			.append(" / ")
			.append(rendicontoRichiesta.getImpegno().getNumero() != null ? rendicontoRichiesta.getImpegno().getNumero().toPlainString() : "")
			.toString();
	}
	
	/**
	 * Calcolo del numero di movimento per rateo
	 * <br/>
	 * Anno del movimento 
	 * 
	 * @param rateo il rateo per cui calcolare il numero di movimento
	 * @return il numero
	 */
	private static final  String calcolaNumeroMovimentoPerEntita(Rateo rateo, PrimaNota primaNota) {
		if(primaNota == null) {
			return "";
		}
		//es : "2016 - Risconto (o Rateo) su anno 2017 - Da Prima Nota N. xxx' 
		StringBuilder sb = new StringBuilder();
		sb.append(FormatUtils.formatDateYear(primaNota.getDataCreazione()));
		sb.append(" - rateo ");
		sb.append("su anno ");
		sb.append(FormatUtils.formatDateYear(rateo.getDataCreazioneRateoRisconto()));
		
		if(primaNota.getListaPrimaNotaPadre() != null  && !primaNota.getListaPrimaNotaPadre().isEmpty()){
			sb.append(" - ");
			sb.append("Da Prima Nota ");
			sb.append(primaNota.getListaPrimaNotaPadre().get(0).getNumeroRegistrazioneLibroGiornale() !=null ? " N.definitivo "+primaNota.getListaPrimaNotaPadre().get(0).getNumeroRegistrazioneLibroGiornale() : "");
			sb.append(primaNota.getListaPrimaNotaPadre().get(0).getNumero() !=null ? " - provvisorio "+primaNota.getListaPrimaNotaPadre().get(0).getNumero() : "");
		}
		return sb.toString();
	}
	
	/**
	 * Calcolo del numero di movimento per  risconto
	 * <br/>
	 * Anno del movimento 
	 * 
	 * @param risconto il risconto per cui calcolare il numero di movimento
	 * @return il numero
	 */
	private static final  String calcolaNumeroMovimentoPerEntita(Risconto risconto, PrimaNota primaNota) {
		if(primaNota == null) {
			return "";
		}
		//es : "2016 - Risconto (o Rateo) su anno 2017 - Da Prima Nota N. xxx' 
		StringBuilder sb = new StringBuilder();
		sb.append(FormatUtils.formatDateYear(primaNota.getDataCreazione()));
		sb.append(" - risconto ");
		sb.append("su anno ");
		sb.append(FormatUtils.formatDateYear(risconto.getDataCreazioneRateoRisconto()));
		
		if(primaNota.getListaPrimaNotaPadre() != null  && !primaNota.getListaPrimaNotaPadre().isEmpty()){
			sb.append(" - ");
			sb.append("Da Prima Nota ");
			sb.append(primaNota.getListaPrimaNotaPadre().get(0).getNumeroRegistrazioneLibroGiornale() !=null ? " N.definitivo "+primaNota.getListaPrimaNotaPadre().get(0).getNumeroRegistrazioneLibroGiornale() : "");
			sb.append(primaNota.getListaPrimaNotaPadre().get(0).getNumero() !=null ? " - provvisorio "+primaNota.getListaPrimaNotaPadre().get(0).getNumero() : "");
		}
		return sb.toString();
	}
}

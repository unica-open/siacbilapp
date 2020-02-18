/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.predocumento;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.BaseFactory;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.PreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Factory per il wrapping dei PreDocumenti.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 24/04/2014
 */
public final class ElementoPreDocumentoFactory extends BaseFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoPreDocumentoFactory() {
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dal PreDocumento.
	 * 
	 * @param preDocumento il predocumento da wrappare
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoPreDocumento getInstance(PreDocumento<?, ?> preDocumento) {
		if(preDocumento instanceof PreDocumentoSpesa) {
			return getInstance((PreDocumentoSpesa)preDocumento);
		}
		if(preDocumento instanceof PreDocumentoEntrata) {
			return getInstance((PreDocumentoEntrata)preDocumento);
		}
		return popolaCampiComuni(preDocumento);
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dal PreDocumento di spesa.
	 * 
	 * @param preDocumento il predocumento da wrappare
	 * 
	 * @return il wrapper creato
	 */
	private static ElementoPreDocumento getInstance(PreDocumentoSpesa preDocumento) {
		ElementoPreDocumento result = popolaCampiComuni(preDocumento);
		
		result.setCausale(preDocumento.getCausaleSpesa());
		result.setContoTesoreria(preDocumento.getContoTesoreria());
		
		return result;
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dal PreDocumento di entrata.
	 * 
	 * @param preDocumento il predocumento da wrappare
	 * 
	 * @return il wrapper creato
	 */
	private static ElementoPreDocumento getInstance(PreDocumentoEntrata preDocumento) {
		ElementoPreDocumento result = popolaCampiComuni(preDocumento);
		
		result.setCausale(preDocumento.getCausaleEntrata());
		result.setContoCorrente(preDocumento.getContoCorrente());
		DatiAnagraficiPreDocumento datiAnagraficiPreDocumento = preDocumento.getDatiAnagraficiPreDocumento();
		if(datiAnagraficiPreDocumento== null){
			return result;
		}
		String ragioneSocialeCognomeNome = calcolaRagioneSocialeNomeECognome(datiAnagraficiPreDocumento);
		result.setRagioneSocialeCognomeNome(ragioneSocialeCognomeNome);
		return result;
	}

	/**
	 * Calcola la ragione sociale prendendo in ingresso i dati anagrafici
	 * @param datiAnagraficiPreDocumento i dati da cui estrarre ragione sociale se presente
	 * altrimenti nome e cognome
	 * @return ragione sociale
	 */
	private static String calcolaRagioneSocialeNomeECognome(DatiAnagraficiPreDocumento datiAnagraficiPreDocumento) {
		if(StringUtils.isNotEmpty(datiAnagraficiPreDocumento.getRagioneSociale())){
			return datiAnagraficiPreDocumento.getRagioneSociale();
		}
		List<String> tempRagioneSociale = new ArrayList<String>();
		if(StringUtils.isNotEmpty(datiAnagraficiPreDocumento.getNome())){
			tempRagioneSociale.add(datiAnagraficiPreDocumento.getNome());
		}
			
		if(StringUtils.isNotEmpty(datiAnagraficiPreDocumento.getCognome())){
			tempRagioneSociale.add(datiAnagraficiPreDocumento.getCognome());
		}
		return StringUtils.join(tempRagioneSociale, " - ");
	}
	
	/**
	 * Popola i campi comuni del Predocumento
	 * 
	 * @param preDocumento il predocumento da wrappare
	 * 
	 * @return il wrapper creato
	 */
	private static <PD extends PreDocumento<?, ?>> ElementoPreDocumento popolaCampiComuni(PD preDocumento) {
		ElementoPreDocumento result = new ElementoPreDocumento();
		
		result.setUid(preDocumento.getUid());
		result.setNumero(preDocumento.getNumero());
		result.setDescrizione(preDocumento.getDescrizione());
		result.setStrutturaAmministrativoContabile(preDocumento.getStrutturaAmministrativoContabile());
		result.setDataCompetenza(preDocumento.getDataCompetenza());
		result.setStatoOperativoPreDocumento(preDocumento.getStatoOperativoPreDocumento());
		result.setDataDocumento(preDocumento.getDataDocumento());
		result.setImporto(preDocumento.getImportoNotNull());
		
		Soggetto soggetto = preDocumento.getSoggetto();
		if(soggetto != null) {
			result.setSoggetto(soggetto.getCodiceSoggetto());
		}
		
		impostaDocumento(result, preDocumento.getSubDocumento());
		
		return result;
	}
	
	/**
	 * Imposta il documento all'interno del wrapper
	 * 
	 * @param wrapper      il wrapper da popolare
	 * @param subDocumento il subdocumento da cui ricavare, se esistente, il documento
	 */
	private static <SD extends Subdocumento<?, ?>> void impostaDocumento(ElementoPreDocumento wrapper, SD subDocumento) {
		String documento = "";
		StatoOperativoDocumento statoOperativoDocumento = null;
		if(subDocumento != null && subDocumento.getDocumento() != null) {
			Documento<?, ?> doc = subDocumento.getDocumento();
			documento = doc.getAnno() + "/" + doc.getTipoDocumento().getCodice() + "/" + doc.getNumero();
			statoOperativoDocumento = doc.getStatoOperativoDocumento();
		}
		wrapper.setDocumento(documento);
		wrapper.setStatoOperativoDocumento(statoOperativoDocumento);
	}

	/**
	 * Ottiene istanze del wrapper a partire da una lista di PreDocumenti.
	 * @param <PD> il tipo del predocumento
	 * @param predocumenti i predocumenti da wrappare
	 * 
	 * @return i wrapper creati
	 */
	public static <PD extends PreDocumento<?, ?>> List<ElementoPreDocumento> getInstances (List<PD> predocumenti) {
		List<ElementoPreDocumento> result = new ArrayList<ElementoPreDocumento>();
		
		for(PD predocumento : predocumenti) {
			result.add(getInstance(predocumento));
		}
		return result;
	}
	
}

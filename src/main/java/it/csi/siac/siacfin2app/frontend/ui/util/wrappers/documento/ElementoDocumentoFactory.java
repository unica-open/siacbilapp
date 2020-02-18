/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.BaseFactory;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Factory per il wrapping dei Documenti.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 10/03/2014
 */
public final class ElementoDocumentoFactory extends BaseFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoDocumentoFactory() {
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dal Documento.
	 * @param <D> la tipizzazione del documento
	 * @param <SD> la tipizzazione del subdocumento
	 * @param documento              il documento da wrappare
	 * @param soggetto               il soggetto da impostare
	 * @param statoOperativoDocPadre del padre
	 * 
	 * @return il wrapper creato
	 */
	public static <D extends Documento<SD, ?>, SD extends Subdocumento<D, ?>> ElementoDocumento getInstance(D documento, Soggetto soggetto, StatoOperativoDocumento statoOperativoDocPadre) {
		ElementoDocumento result = new ElementoDocumento();
		
		populateBaseInstance(result, documento, soggetto, statoOperativoDocPadre);
		
		return result;
	}
	
	/**
	 * Popola l'istanza con i dati di base
	 * @param <D> la tipizzazione del documento
	 * @param <SD> la tipizzazione del subdocumento
	 * @param wrapper                il wrapper da popolare
	 * @param documento              il documento da wrappare
	 * @param soggetto               il soggetto da impostare
	 * @param statoOperativoDocPadre del padre
	 */
	private static <D extends Documento<SD, ?>, SD extends Subdocumento<D, ?>> void populateBaseInstance(ElementoDocumento wrapper, D documento, Soggetto soggetto, StatoOperativoDocumento statoOperativoDocPadre) {
		
		if(documento == null || documento.getTipoDocumento() == null){
			return;
		}
		
		Integer uid = documento.getUid();
		
		String doc = new StringBuilder()
				.append(documento.getAnno())
				.append("/")
				.append(documento.getTipoDocumento().getCodice().toUpperCase())
				.append("/")
				.append(documento.getNumero())
				.toString();

		String data = FormatUtils.formatDate(documento.getDataEmissione());
		
		//!= null? getSubAccertamento().getNumero() : null
				
		String statoOperativoDocumentoCode = documento.getStatoOperativoDocumento()!=null && documento.getStatoOperativoDocumento().getCodice() !=null ? documento.getStatoOperativoDocumento().getCodice() : "" ;
		String statoOperativoDocumentoDesc = documento.getStatoOperativoDocumento()!=null && documento.getStatoOperativoDocumento().getDescrizione() !=null ? documento.getStatoOperativoDocumento().getDescrizione() : "";
		String statoOperativoDocumentoPadreCode = statoOperativoDocPadre != null &&  statoOperativoDocPadre.getCodice() !=null ? statoOperativoDocPadre.getCodice() : "" ;
		
		String soggettoWrapper = "";
		
		if(soggetto != null) {
			soggettoWrapper = soggetto.getCodiceSoggetto().concat("-").concat(soggetto.getDenominazione());
		}
		
		String tipoDocumentoNotaCredito = documento.getTipoDocumento().getCodiceGruppo();
		BigDecimal importo = documento.getImporto();
		BigDecimal importoDaDedurreSuFattura = documento.getImportoDaDedurreSuFattura();
		TipoRelazione tipoRelazione = documento.getTipoRelazione();
				
		wrapper.setUid(uid);
		wrapper.setDocumento(doc);
		wrapper.setData(data);
		wrapper.setStatoOperativoDocumentoCode(statoOperativoDocumentoCode);
		wrapper.setStatoOperativoDocumentoDesc(statoOperativoDocumentoDesc);
		wrapper.setFlagAttivaGen(Boolean.TRUE.equals(documento.getTipoDocumento().getFlagAttivaGEN()));
		wrapper.setFlagComunicaPCC(Boolean.TRUE.equals(documento.getTipoDocumento().getFlagComunicaPCC()));
		//SIAC-5617
		wrapper.setTipoDocumentoCode(documento.getTipoDocumento().getCodice());
		wrapper.setContabilizzaGenPCC(Boolean.TRUE.equals(documento.getContabilizzaGenPcc()));
		wrapper.setSoggetto(soggettoWrapper);
		wrapper.setTipoDocumentoNotaCredito(tipoDocumentoNotaCredito);
		wrapper.setImporto(importo);
		wrapper.setImportoDaDedurreSuFattura(importoDaDedurreSuFattura);
		wrapper.setStatoOperativoDocumentoPadreCode(statoOperativoDocumentoPadreCode);
		wrapper.setTipoRelazione(tipoRelazione);
		
		//SIAC-6565-CR1215
		wrapper.setStatoSDI(documento.getStatoSDI());
		wrapper.setEsitoStatoSDI(documento.getEsitoStatoSDI());
		
		// Controllo allegato atto
		controlloAllegatoAtto(wrapper, documento);
	}
	
	/**
	 * Controlla che il documento sia collegato o meno all'allegato atto.
	 *  @param <D> la tipizzazione del documento
	 * @param <SD> la tipizzazione del subdocumento
	 * @param instance il wrapper da popolare
	 * @param doc      il documento originale
	 */
	private static <D extends Documento<SD, ?>, SD extends Subdocumento<D, ?>> void controlloAllegatoAtto(ElementoDocumento instance, D doc) {
		 instance.setTipoALG("ALG".equalsIgnoreCase(doc.getTipoDocumento().getCodice()));
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dal Documento.
	 * @param <D> la tipizzazione del documento
	 * @param <SD> la tipizzazione del subdocumento
	 * @param documento il documento da wrappare
	 * 
	 * @return il wrapper creato
	 */
	public static <D extends Documento<SD, ?>, SD extends Subdocumento<D, ?>> ElementoDocumento getInstance(D documento) {
		return getInstance(documento, documento.getSoggetto(), documento.getStatoOperativoDocumento());
	}
	
   /**
    * 
	 * Ottiene un'istanza del wrapper a partire da una lista di documenti e dal soggetto padre
	 * @param <D> la tipizzazione del documento
	 * @param <SD> la tipizzazione del subdocumento
	 * @param documenti i documenti da wrappare
	 * @param soggetto  il soggetto da impostare
	 * 
	 * @return il wrapper creato
	 */
	public static <D extends Documento<SD, ?>, SD extends Subdocumento<D, ?>> List<ElementoDocumento> getInstances (List<D> documenti, Soggetto soggetto) {
		List<ElementoDocumento> result = new ArrayList<ElementoDocumento>();
		
		for(D documento : documenti) {
			result.add(getInstance(documento, soggetto, documento.getStatoOperativoDocumento()));
		}
		return result;
	}
	
	
	/**
	 * Ottiene un'istanza del wrapper a partire da una lista di documenti e dal soggetto padre
	 * @param <D> la tipizzazione del documento
	 * @param <SD> la tipizzazione del subdocumento
	 * @param documenti              i documenti da wrappare
	 * @param soggetto               il soggetto da impostare
	 * @param statoOperativoDocPadre del documento padre della nota, per la discriminazione sull'aggiornamento
	 * 
	 *  @return il wrapper creato
	 */
	public static <D extends Documento<SD, ?>, SD extends Subdocumento<D, ?>> List<ElementoDocumento> getInstances (List<D> documenti, Soggetto soggetto, StatoOperativoDocumento statoOperativoDocPadre) {
		List<ElementoDocumento> result = new ArrayList<ElementoDocumento>();
		
		for(D documento : documenti) {
			result.add(getInstance(documento, soggetto, statoOperativoDocPadre));
		}
		return result;
	}
		
	
	/**
	 * Ottiene un'istanza del wrapper a partire da un documento
	 * @param <D> la tipizzazione del documento
	 * @param <SD> la tipizzazione del subdocumento
	 * @param documenti i documenti da wrappare
	 * 
	 * @return il wrapper creato
	 */
	public static <D extends Documento<SD, ?>, SD extends Subdocumento<D, ?>> List<ElementoDocumento> getInstances (List<D> documenti) {
		List<ElementoDocumento> result = new ArrayList<ElementoDocumento>();
			
		for(D documento : documenti) {
			result.add(getInstance(documento, documento.getSoggetto(), documento.getStatoOperativoDocumento()));
		}
		return result;
	}
	
	/**
	 * Ottiene un'istanza del wrapper per l'entrata.
	 * 
	 * @param documento il documento da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ElementoDocumentoEntrata getInstanceEntrata(DocumentoEntrata documento) {
		ElementoDocumentoEntrata wrapper = new ElementoDocumentoEntrata();
		
		wrapper.setDocumentoEntrata(documento);
		
		populateBaseInstance(wrapper, documento, documento.getSoggetto(), documento.getStatoOperativoDocumento());
		return wrapper;
	}
	
	/**
	 * Ottiene un'istanza del wrapper per la spesa.
	 * 
	 * @param documento il documento da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ElementoDocumentoSpesa getInstanceSpesa(DocumentoSpesa documento) {
		ElementoDocumentoSpesa wrapper = new ElementoDocumentoSpesa();
		
		wrapper.setDocumentoSpesa(documento);
		
		populateBaseInstance(wrapper, documento, documento.getSoggetto(), documento.getStatoOperativoDocumento());
		return wrapper;
	}
}

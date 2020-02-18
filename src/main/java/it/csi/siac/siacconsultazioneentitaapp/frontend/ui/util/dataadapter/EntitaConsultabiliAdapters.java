/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siaccecser.model.CECDataDictionary;
import it.csi.siac.siaccommonapp.util.exception.FrontEndUncheckedException;
import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;

/**
 * Enumera gli adapter delle colonne associati ad ogni {@link TipoEntitaConsultabile}.
 * 
 * @author Domenico Lisi
 * @version 1.0 - 22/03/2016
 */
@XmlType(namespace = CECDataDictionary.NAMESPACE)
public enum EntitaConsultabiliAdapters {

	/** Data wrapper per il Soggetto */
	SOGGETTO(TipoEntitaConsultabile.SOGGETTO, SoggettoDataAdapter.class),
	/** Data wrapper per il Provvedimento */
	PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, ProvvedimentoDataAdapter.class),
	
	/** Data wrapper per il Capitolo di spesa (Uscita Previsione - Uscita Gestione) */
	CAPITOLOSPESA(TipoEntitaConsultabile.CAPITOLOSPESA, CapitoloSpesaDataAdapter.class),
	/** Data wrapper per il Capitolo di entrata (Entrata Previsione - Entrata Gestione) */
	CAPITOLOENTRATA(TipoEntitaConsultabile.CAPITOLOENTRATA, CapitoloEntrataDataAdapter.class),
	
	/** Data wrapper per l'Accertamento */
	ACCERTAMENTO(TipoEntitaConsultabile.ACCERTAMENTO, AccertamentoDataAdapter.class),
	/** Data wrapper per la Reversale (Ordinativo di entrata) */
	REVERSALE(TipoEntitaConsultabile.REVERSALE, ReversaleDataAdapter.class),
	
	/** Data wrapper per l'Impegno */
	IMPEGNO(TipoEntitaConsultabile.IMPEGNO, ImpegnoDataAdapter.class),
	/** Data wrapper per la Liquidazione */
	LIQUIDAZIONE(TipoEntitaConsultabile.LIQUIDAZIONE, LiquidazioneDataAdapter.class),
	/** Data wrapper per il Mandato (Ordinativo di Spesa) */
	MANDATO(TipoEntitaConsultabile.MANDATO, MandatoDataAdapter.class),
	
	/** Data wrapper per l'Allegato (Atto) */
	ALLEGATO(TipoEntitaConsultabile.ALLEGATO, AllegatoDataAdapter.class),
	/** Data wrapper per l'Elenco (Documenti) */
	ELENCO(TipoEntitaConsultabile.ELENCO, ElencoDataAdapter.class),
	/** Data wrapper per il Documento (Spesa - Entrata) */
	DOCUMENTO(TipoEntitaConsultabile.DOCUMENTO, DocumentoDataAdapter.class),
	/** Data wrapper per la Variazione */
	VARIAZIONE(TipoEntitaConsultabile.VARIAZIONE, VariazioneDataAdapter.class), 

	// SIAC-4589
	/** Data wrapper per la Modifica di importo per il movimenti di gestione di spesa */
	MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_SPESA(TipoEntitaConsultabile.MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_SPESA, ModificaImportoMovimentoGestioneSpesaDataAdapter.class),
	/** Data wrapper per la Modifica di importo per il movimenti di gestione di entrata */
	MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_ENTRATA(TipoEntitaConsultabile.MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_ENTRATA, ModificaImportoMovimentoGestioneEntrataDataAdapter.class),
	
	// SIAC-5279
	/** Data wrapper per l'Indirizzo */
	INDIRIZZO(TipoEntitaConsultabile.INDIRIZZO, IndirizzoDataAdapter.class),
	/** Data wrapper per la Sede secondaria del soggetto */
	SEDE_SECONDARIA_SOGGETTO(TipoEntitaConsultabile.SEDE_SECONDARIA_SOGGETTO, SedeSecondariaSoggettoDataAdapter.class),
	/** Data wrapper per la Modalit&agrave; di pagamento del soggetto */
	MODALITA_PAGAMENTO_SOGGETTO(TipoEntitaConsultabile.MODALITA_PAGAMENTO_SOGGETTO, ModalitaPagamentoSoggettoDataAdapter.class),
	;
	
	private final TipoEntitaConsultabile tipoEntitaConsultabile;
	private Class<? extends EntitaConsultabileDataAdapter> entitaConsultabileDataAdapterClass;
	
	/**
	 * Costruttore.
	 * 
	 * @param tipoEntitaConsultabile il tipo di entit&agrave; consultabile
	 * @param entitaConsultabileDataAdapterClass la classe del data adapter
	 */
	private EntitaConsultabiliAdapters(TipoEntitaConsultabile tipoEntitaConsultabile, Class<? extends EntitaConsultabileDataAdapter> entitaConsultabileDataAdapterClass) {
		this.tipoEntitaConsultabile = tipoEntitaConsultabile;
		this.entitaConsultabileDataAdapterClass = entitaConsultabileDataAdapterClass;
	}
	
	/**
	 * Ottiene un'istanza a partire dal tipo di entit&agrave; consiltabile
	 * @param tipoEntitaConsultabile il tipo di entit&agrave; da cui ottenere l'istanza
	 * @return l'istanza corrispondente
	 * @throws IllegalArgumentException nel caso in cui non vi sia un mapping
	 */
	public static EntitaConsultabiliAdapters byTipoEntitaConsultabile(TipoEntitaConsultabile tipoEntitaConsultabile){
		for(EntitaConsultabiliAdapters e : EntitaConsultabiliAdapters.values()){
			if(e.getTipoEntitaConsultabile().equals(tipoEntitaConsultabile)){
				return e;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping per il TipoEntitaConsultabile: "+ tipoEntitaConsultabile + " in TipoEntitaConsultabili");
		
	}

	/**
	 * @return the tipoEntitaConsultabile
	 */
	public TipoEntitaConsultabile getTipoEntitaConsultabile() {
		return tipoEntitaConsultabile;
	}
	
	/**
	 * Ottiene una nuova istanza del data adapter.
	 * @param <T> la tipizzazione dell'entit&agrave; consultabile
	 * @return l'istanza cercata
	 * @throws FrontEndUncheckedException nel caso in cui non fosse possibile instanziare l'adapter
	 */
	@SuppressWarnings("unchecked")
	public <T extends EntitaConsultabileDataAdapter> T newEntitaConsultabileDataAdapterInstance() {
		try {
			return (T) entitaConsultabileDataAdapterClass.newInstance();
		} catch (InstantiationException e) {
			throw new FrontEndUncheckedException("Eccezione istanziamento DataAdapter EntitaConsultabili." + name() + "->" + entitaConsultabileDataAdapterClass + " ["+tipoEntitaConsultabile+"]", e);
		} catch (IllegalAccessException e) {
			throw new FrontEndUncheckedException("Impossibile accedere al costruttore vuoto del DataAdapter di " + entitaConsultabileDataAdapterClass + " [" + tipoEntitaConsultabile + "]", e);
		}
	}
	
	/**
	 * Istanzia un adapter corrispondente al tipo di entit&agrave; consultabile.
	 * @param <T> la tipizzazione dell'entit&agrave; consultabile
	 * @param tipoEntitaConsultabile il tipo di entit&agrave; da cui reperire l'istanza
	 * @return l'istanza dell'adapter
	 * @throws IllegalArgumentException nel caso in cui non vi sia un mapping epr il tipo di entit&agrave;
	 * @throws FrontEndUncheckedException nel caso in cui non fosse possibile instanziare l'adapter
	 */
	public static <T extends EntitaConsultabileDataAdapter> T newEntitaConsultabileDataAdapterInstance(TipoEntitaConsultabile tipoEntitaConsultabile) {
		EntitaConsultabiliAdapters entitaConsultabili = byTipoEntitaConsultabile(tipoEntitaConsultabile);
		return entitaConsultabili.newEntitaConsultabileDataAdapterInstance();
		
	}
	
	
}
